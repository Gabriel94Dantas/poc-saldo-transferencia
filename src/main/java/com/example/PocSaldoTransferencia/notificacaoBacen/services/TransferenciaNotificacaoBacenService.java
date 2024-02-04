package com.example.PocSaldoTransferencia.notificacaoBacen.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.PocSaldoTransferencia.notificacaoBacen.dtos.TransferenciaDto;
import com.example.PocSaldoTransferencia.notificacaoBacen.dtos.requests.TransferenciaRequestDto;
import com.example.PocSaldoTransferencia.notificacaoBacen.mocks.BacenMockedResponses;

@Service
public class TransferenciaNotificacaoBacenService {

    private BacenMockedResponses bacenMockedResponses;

    public TransferenciaNotificacaoBacenService(BacenMockedResponses bacenMockedResponses){
        this.bacenMockedResponses = bacenMockedResponses;
    }

    public void notificarBacen(TransferenciaDto transferenciaDto) throws InterruptedException{
        TransferenciaRequestDto transferenciaRequestDto = new TransferenciaRequestDto();
        transferenciaRequestDto.setAgencia(transferenciaDto.getAgencia());
        transferenciaRequestDto.setConta(transferenciaDto.getConta());
        transferenciaRequestDto.setAgenciaDestino(transferenciaDto.getAgenciaDestino());
        transferenciaRequestDto.setContaDestino(transferenciaDto.getContaDestino());
        transferenciaRequestDto.setInstituicaoFinanceira("1000");
        transferenciaRequestDto.setInstituicaoFinanceiraDestino(transferenciaDto.getInstituicaoDestino());
        transferenciaRequestDto.setValor(transferenciaDto.getValor().toString());
        
        ResponseEntity<String> res = bacenMockedResponses.notificacaoBacen(transferenciaRequestDto);
        while (res.getStatusCode() != HttpStatus.OK) {
            Thread.sleep(1000);
            res = bacenMockedResponses.notificacaoBacen(transferenciaRequestDto);
        }
        
    }

}
