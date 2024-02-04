package com.example.PocSaldoTransferencia.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.PocSaldoTransferencia.entities.Saldo;

@Repository
public interface SaldoRepository extends GenericRepository<Saldo, Integer> {

    @Query(value = "select * from desafio.saldo where agencia = ? and conta = ?", 
        nativeQuery = true)
    Saldo findByAgenciaAndConta(String agencia, String conta);
}
