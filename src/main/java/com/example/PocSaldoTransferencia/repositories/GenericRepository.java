package com.example.PocSaldoTransferencia.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.yaml.snakeyaml.events.Event.ID;

@SuppressWarnings("hiding")
@NoRepositoryBean
public interface GenericRepository<T,ID> extends Repository<T, ID> {

    Iterable<T> findById(ID id);
    boolean existsById(ID id);
    Iterable<T> findAll();
    Iterable<T> findAllById(Iterable<ID> ids);
    long count();

}
