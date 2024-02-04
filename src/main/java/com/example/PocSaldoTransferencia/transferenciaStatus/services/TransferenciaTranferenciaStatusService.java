package com.example.PocSaldoTransferencia.transferenciaStatus.services;

import org.springframework.stereotype.Service;

import com.example.PocSaldoTransferencia.entities.Transferencia;
import com.example.PocSaldoTransferencia.repositories.TransferenciaRespository;
import com.example.PocSaldoTransferencia.transferenciaStatus.dtos.TransferenciaDto;

@Service
public class TransferenciaTranferenciaStatusService {
    
    private TransferenciaRespository transferenciaRespository;

    public TransferenciaTranferenciaStatusService(TransferenciaRespository transferenciaRespository){
        this.transferenciaRespository = transferenciaRespository;
    }

    public void mudancaStatus(TransferenciaDto transferenciaDto){
        Transferencia transferencia = new Transferencia();
        transferencia.setAgencia(transferenciaDto.getAgencia());
        transferencia.setConta(transferenciaDto.getConta());
        transferencia.setAgenciaDestino(transferenciaDto.getAgenciaDestino());
        transferencia.setContaDestino(transferenciaDto.getContaDestino());
        transferencia.setDataTransacao(transferenciaDto.getDataTransacao());
        transferencia.setId(transferenciaDto.getId());
        transferencia.setInstituicaoDestino(transferenciaDto.getInstituicaoDestino());
        transferencia.setStatus(transferenciaDto.getStatus());
        transferencia.setValor(transferenciaDto.getValor());

        transferenciaRespository.save(transferencia);
    }

}
