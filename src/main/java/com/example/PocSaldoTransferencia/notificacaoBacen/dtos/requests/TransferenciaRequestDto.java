package com.example.PocSaldoTransferencia.notificacaoBacen.dtos.requests;

import lombok.Data;

@Data
public class TransferenciaRequestDto {
    
    private String agencia;
    private String conta;
    private String instituicaoFinanceira;
    private String agenciaDestino;
    private String contaDestino;
    private String instituicaoFinanceiraDestino;
    private String valor;

}
