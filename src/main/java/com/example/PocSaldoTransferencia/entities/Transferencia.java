package com.example.PocSaldoTransferencia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transferencia", schema = "desafio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transferencia {
    
    @Id
    private Integer id;
    private String agencia;
    private String conta;
    private String agenciaDestino;
    private String contaDestino;
    private String instituicaoDestino;
    private String status;
    private Double valor;

}
