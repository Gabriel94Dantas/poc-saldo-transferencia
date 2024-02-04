package com.example.PocSaldoTransferencia.services;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.PocSaldoTransferencia.contexts.KafkaContext;
import com.example.PocSaldoTransferencia.contexts.RedisContext;
import com.example.PocSaldoTransferencia.dtos.requests.TransferenciaRequestDto;
import com.example.PocSaldoTransferencia.dtos.responses.TransferenciaResponseDto;
import com.example.PocSaldoTransferencia.entities.Saldo;
import com.example.PocSaldoTransferencia.entities.Transferencia;
import com.example.PocSaldoTransferencia.mocks.entities.ClienteMock;
import com.example.PocSaldoTransferencia.mocks.services.ClienteMockService;
import com.example.PocSaldoTransferencia.producers.EventProducer;
import com.example.PocSaldoTransferencia.repositories.SaldoRepository;
import com.example.PocSaldoTransferencia.repositories.TransferenciaRespository;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Service
@Slf4j
public class TransferenciaService {
    
    private TransferenciaRespository transferenciaRespository;
    private SaldoRepository saldoRepository;


    private ClienteMockService clienteMockService;

    private RedisContext redisContext;
    private KafkaContext kafkaContext;
    
    public TransferenciaService (TransferenciaRespository transferenciaRespository, 
        SaldoRepository saldoRepository, RedisContext redisContext,
        KafkaContext kafkaContext){
        this.transferenciaRespository = transferenciaRespository;
        this.saldoRepository = saldoRepository;
        this.redisContext = redisContext;
        this.kafkaContext = kafkaContext;
        this.clienteMockService = new ClienteMockService();
        clienteMockService.initializeMock();
    }

    public TransferenciaResponseDto transferir(TransferenciaRequestDto transferenciaRequestDto) throws IOException{
        ClienteMock clienteMock = buscarCliente(transferenciaRequestDto.getAgencia(), transferenciaRequestDto.getConta());
        if(clienteMock == null){
            TransferenciaResponseDto transferenciaResponseDto = new TransferenciaResponseDto();
            transferenciaResponseDto.setMensagemCliente("Cliente não encontrado ou inativo");
            return transferenciaResponseDto;
        }

        if(!isLimiteDiarioSuficiente(transferenciaRequestDto.getAgencia(), transferenciaRequestDto.getConta(),
             Double.valueOf(transferenciaRequestDto.getValor()))){
            TransferenciaResponseDto transferenciaResponseDto = new TransferenciaResponseDto();
            transferenciaResponseDto.setMensagemCliente("Limite Diário Insuficiente");
            return transferenciaResponseDto;
        }

        if(!isSaldoSuficiente(transferenciaRequestDto.getAgencia(), transferenciaRequestDto.getConta(), 
            Double.valueOf(transferenciaRequestDto.getValor()))){
            TransferenciaResponseDto transferenciaResponseDto = new TransferenciaResponseDto();
            transferenciaResponseDto.setMensagemCliente("Saldo Insuficiente");
            return transferenciaResponseDto;
        }

        Transferencia transferencia = enviarTransferenciaBacen(transferenciaRequestDto);
        Saldo saldo = atualizarSaldo(transferencia);
        transferenciaRespository.save(transferencia);
        return carregarDadosDeResposta(transferencia, saldo);

    }

    public ClienteMock buscarCliente(String agencia, String conta){
        return clienteMockService.searchClientByAgAcc(agencia, conta);
    }

    public boolean isSaldoSuficiente(String agencia, String conta, Double valor){
        Saldo saldo = saldoRepository.findByAgenciaAndConta(agencia, conta);
        return saldo.getValor() >= valor;
    }

    public boolean isLimiteDiarioSuficiente(String agencia, String conta, Double valor){
        
        if(valor > 1000){
            return false;
        }

        Jedis jedis = redisContext.getConnection();
        String limiteDiario = jedis.get(agencia+conta);
        if(limiteDiario == null){
            Double novoLimiteDiario = 1000.0 - valor;
            jedis.set(agencia+conta, novoLimiteDiario.toString());
            jedis.expire(agencia+conta, calcularTempoFinalDia());
            return true;
        }

        Double novoLimiteDiario = Double.parseDouble(limiteDiario) - valor;

        if(novoLimiteDiario >= 0){
            jedis.set(agencia+conta, novoLimiteDiario.toString());
            jedis.expire(agencia + conta, calcularTempoFinalDia());
        }

        jedis.close();
        redisContext.close();
        return novoLimiteDiario >= 0;
    }

    public Transferencia enviarTransferenciaBacen(TransferenciaRequestDto transferenciaRequestDto) throws IOException{
        Transferencia transferencia = new Transferencia();
        transferencia.setAgencia(transferenciaRequestDto.getAgencia());
        transferencia.setAgenciaDestino(transferenciaRequestDto.getAgenciaDestino());
        transferencia.setConta(transferenciaRequestDto.getConta());
        transferencia.setContaDestino(transferenciaRequestDto.getContaDestino());
        transferencia.setDataTransacao(new Date());
        transferencia.setInstituicaoDestino(transferenciaRequestDto.getInstituicaoDestino());
        transferencia.setStatus("Aguardando Notificação BACEN");
        transferencia.setValor(Double.parseDouble(transferenciaRequestDto.getValor()));
        transferencia.setId(transferenciaRespository.count() + 1);

        EventProducer eventProducer = new EventProducer(this.kafkaContext);
        try {
            eventProducer.send(transferencia);
        } catch (Exception e) {
            log.error("Error to send to BACEN", e);
            throw new RuntimeException(e);
        }finally{
            eventProducer.close();
        }

        return transferencia;
    }

    public Saldo atualizarSaldo(Transferencia transferencia){
        Saldo saldo = saldoRepository.findByAgenciaAndConta(transferencia.getAgencia(), transferencia.getConta());
        saldo.setValor(saldo.getValor() - transferencia.getValor());
        saldoRepository.save(saldo);
        return saldo;
    }

    public TransferenciaResponseDto carregarDadosDeResposta(Transferencia transferencia, Saldo saldo){
        TransferenciaResponseDto transferenciaResponseDto = new TransferenciaResponseDto();
        transferenciaResponseDto.setAgencia(transferencia.getAgencia());
        transferenciaResponseDto.setConta(transferencia.getConta());
        transferenciaResponseDto.setMensagemCliente("Transferência executada com sucesso");
        transferenciaResponseDto.setSaldo(saldo.getValor().toString());
        transferenciaResponseDto.setStatus(transferencia.getStatus());

        return transferenciaResponseDto;
    }

    public long calcularTempoFinalDia(){
        ZoneId z = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime now = ZonedDateTime.now(z);
        LocalDate today = now.toLocalDate();
        ZonedDateTime startOfTomorrow = today.plusDays( 1 ).atStartOfDay(z);
        Duration d = Duration.between( now , startOfTomorrow );
        return d.toMillis()/1000;
    }
}
