package com.example.PocSaldoTransferencia.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saldo", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Saldo {
    
    @Id
    private Integer id;
    private String agencia;
    private String conta;
    private Double valor;
    private Date ultimaTransacao;

}
