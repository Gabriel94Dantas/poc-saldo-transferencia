package com.example.PocSaldoTransferencia.notificacaoBacen.mocks;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.example.PocSaldoTransferencia.notificacaoBacen.dtos.requests.TransferenciaRequestDto;

@Component
public class BacenMockedResponses {
    
    public ResponseEntity<String> notificacaoBacen(TransferenciaRequestDto transferenciaRequestDto){
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 11);

        if(randomNumber % 3 == 0){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("{\"msg\": \"Too Many Requests\"}");
        }

        return ResponseEntity.status(HttpStatus.OK).body("{\"msg\": \"Sucess\"}");
    }

}
