package com.example.PocSaldoTransferencia.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaRequestDto {
    
    private String agencia;
    private String conta;
    private String agenciaDestino;
    private String contaDestino;
    private String instituicaoDestino;
    private String valor;

}
