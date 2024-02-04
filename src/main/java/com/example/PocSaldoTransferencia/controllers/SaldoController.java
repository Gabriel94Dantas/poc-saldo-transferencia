package com.example.PocSaldoTransferencia.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PocSaldoTransferencia.dtos.requests.SaldoRequestDto;
import com.example.PocSaldoTransferencia.dtos.responses.SaldoResponseDto;
import com.example.PocSaldoTransferencia.services.SaldoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/saldo")
public class SaldoController {
    
    private SaldoService saldoService;

    public SaldoController(SaldoService saldoService){
        this.saldoService = saldoService;
    }
    
    @Operation(summary = "Consulta de Saldo")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Saldo Retornado", 
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = SaldoResponseDto.class)) }), 
        @ApiResponse(responseCode = "404", description = "Cliente não Encontrado", 
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = SaldoResponseDto.class)) }), 
        })
    @GetMapping(value = "/consulta", produces = "application/json")
    public ResponseEntity<SaldoResponseDto> getSaldo(@ParameterObject final SaldoRequestDto saldoRequestDto){
        SaldoResponseDto saldoResponseDto = saldoService.consultarSaldo(saldoRequestDto);
        if(saldoResponseDto.getAgencia() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(saldoResponseDto);
        }        
        return ResponseEntity.ok(saldoResponseDto);
    }


    @Operation(summary = "Criar Massa de Testes")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Saldos Criados", 
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Cliente não Encontrado", 
            content = @Content(mediaType = "application/json")) 
        })
    @PostMapping(value = "/massaTeste")
    public ResponseEntity<String> setMassaDados(){
        saldoService.createSampleData();
        return ResponseEntity.status(HttpStatus.CREATED).body("Sample criada com sucesso");
    }
}