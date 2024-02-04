package com.example.PocSaldoTransferencia.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PocSaldoTransferencia.dtos.requests.TransferenciaRequestDto;
import com.example.PocSaldoTransferencia.dtos.responses.TransferenciaResponseDto;
import com.example.PocSaldoTransferencia.services.TransferenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/transferecia")
@Slf4j
public class TransferenciaController {
    
    public TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService){
        this.transferenciaService = transferenciaService;
    }

    @Operation(summary = "Executar Transferência")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Transferência Executada", 
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = TransferenciaResponseDto.class)) }), 
        @ApiResponse(responseCode = "401", description = "Transferência não autorizada", 
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = TransferenciaResponseDto.class)) }), 
        })
    @PostMapping(produces="application/json", consumes = "application/json")
    public ResponseEntity<TransferenciaResponseDto> transferir(@RequestBody final TransferenciaRequestDto transferenciaRequestDto){
        TransferenciaResponseDto transferenciaResponseDto = null;
        try {
            transferenciaResponseDto = transferenciaService.transferir(transferenciaRequestDto);
            if(transferenciaResponseDto.getAgencia() == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transferenciaResponseDto);
            }
        } catch (Exception e) {
            log.error("Error to transfer", e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(transferenciaResponseDto);
    }
}
