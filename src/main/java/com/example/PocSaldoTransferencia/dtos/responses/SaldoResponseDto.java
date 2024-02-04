package com.example.PocSaldoTransferencia.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaldoResponseDto {
    
    private String agencia;
    private String conta;
    private String nome;
    private String valor;
    private String mensagemCliente;

}
