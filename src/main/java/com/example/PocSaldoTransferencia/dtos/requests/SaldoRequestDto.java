package com.example.PocSaldoTransferencia.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaldoRequestDto {
    
    private String agencia;
    private String conta;
}
