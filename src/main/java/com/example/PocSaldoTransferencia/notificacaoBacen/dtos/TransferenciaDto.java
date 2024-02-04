package com.example.PocSaldoTransferencia.notificacaoBacen.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDto {
    
    private Integer id;
    private String agencia;
    private String conta;
    private String agenciaDestino;
    private String contaDestino;
    private String instituicaoDestino;
    private String status;
    private Double valor;
    private Date dataTransacao;

}
