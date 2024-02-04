package com.example.PocSaldoTransferencia.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaResponseDto {
    
    private String saldo;
    private String status;
    private String agencia;
    private String conta;
    private String mensagemCliente;

}
