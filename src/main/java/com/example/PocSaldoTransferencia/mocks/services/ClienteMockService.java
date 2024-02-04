package com.example.PocSaldoTransferencia.mocks.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.PocSaldoTransferencia.mocks.entities.ClienteMock;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class ClienteMockService {
    
    private List<ClienteMock> clientes;

    public void initializeMock(){
        ClienteMock clienteMock = new ClienteMock();
        clientes = clienteMock.createListaClientes();
    }

    public ClienteMock searchClientByAgAcc(String agencia, String conta){
        List<ClienteMock> clis = new ArrayList<ClienteMock>();
        clientes.stream().forEach(c -> {
            if(c.getAgencia().equals(agencia) && c.getConta().equals(conta) &&  c.getAtivo()){
                clis.add(c);
            }
        });
        if(clis != null && !clis.isEmpty()){
            return clis.get(0);
        }
        return null;
    }

}
