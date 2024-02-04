package com.example.PocSaldoTransferencia.services;

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
}
