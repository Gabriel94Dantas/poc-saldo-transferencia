package com.example.PocSaldoTransferencia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
    
    private String nome;
    private String cpf;
    private String conta;
    private String agencia;
    private Boolean ativo;

}
