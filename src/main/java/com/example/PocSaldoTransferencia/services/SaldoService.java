package com.example.PocSaldoTransferencia.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.PocSaldoTransferencia.dtos.requests.SaldoRequestDto;
import com.example.PocSaldoTransferencia.dtos.responses.SaldoResponseDto;
import com.example.PocSaldoTransferencia.entities.Saldo;
import com.example.PocSaldoTransferencia.mocks.entities.ClienteMock;
import com.example.PocSaldoTransferencia.mocks.services.ClienteMockService;
import com.example.PocSaldoTransferencia.repositories.SaldoRepository;

@Service
public class SaldoService {

    private SaldoRepository saldoRepository;
    private ClienteMockService clienteMockService;

    public SaldoService(SaldoRepository saldoRepository){
        this.saldoRepository = saldoRepository;
        this.clienteMockService = new ClienteMockService();
        clienteMockService.initializeMock();
    }
    
    public SaldoResponseDto consultarSaldo(SaldoRequestDto saldoRequestDto){
        ClienteMock clienteMock = clienteMockService.searchClientByAgAcc(saldoRequestDto.getAgencia(), saldoRequestDto.getConta());
        
        if(clienteMock == null){
            SaldoResponseDto saldoResponseDto = new SaldoResponseDto();
            saldoResponseDto.setMensagemCliente("Cliente Inativo ou não encontrado");
            return saldoResponseDto;
        }

        Saldo saldo = saldoRepository.findByAgenciaAndConta(saldoRequestDto.getAgencia(), saldoRequestDto.getConta());
        return new SaldoResponseDto(clienteMock.getAgencia(), clienteMock.getConta(),
            clienteMock.getNome(), saldo.getValor().toString(), "Saldo Consultado com Sucesso");
    }

    public void saveSaldo(Saldo saldo){
        saldoRepository.save(saldo);
    }

    public void createSampleData(){
        Saldo saldo1 = new Saldo(1, "1111", "111111", 27678.87, new Date());
        Saldo saldo2 = new Saldo(2, "2222", "222222", 560.76, new Date());
        Saldo saldo3 = new Saldo(3, "3333", "333333", 45.98, new Date());
        Saldo saldo4 = new Saldo(4, "4444", "444444", 154368.93, new Date());
        Saldo saldo5 = new Saldo(5, "5555", "555555", 12.0, new Date());

        List<Saldo> saldos = new ArrayList<Saldo>();
        saldos.add(saldo1);
        saldos.add(saldo2);
        saldos.add(saldo3);
        saldos.add(saldo4);
        saldos.add(saldo5);

        saldoRepository.saveAll(saldos);
    }
}
