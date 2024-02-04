package com.example.PocSaldoTransferencia.mocks.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteMock {
    
    private String nome;
    private String cpf;
    private String conta;
    private String agencia;
    private Boolean ativo;

    public List<ClienteMock> createListaClientes(){
        ClienteMock clienteMock1 = new ClienteMock("teste1", "11111111111", "111111", "1111", true);
        ClienteMock clienteMock2 = new ClienteMock("teste2", "22222222222", "222222", "2222", true);
        ClienteMock clienteMock3 = new ClienteMock("teste3", "33333333333", "333333", "3333", false);
        ClienteMock clienteMock4 = new ClienteMock("teste4", "44444444444", "444444", "4444", true);
        ClienteMock clienteMock5 = new ClienteMock("teste5", "55555555555", "555555", "5555", true);

        List<ClienteMock> clientes = new ArrayList<ClienteMock>();
        clientes.add(clienteMock1);
        clientes.add(clienteMock2);
        clientes.add(clienteMock3);
        clientes.add(clienteMock4);
        clientes.add(clienteMock5);

        return clientes;
    }

}
