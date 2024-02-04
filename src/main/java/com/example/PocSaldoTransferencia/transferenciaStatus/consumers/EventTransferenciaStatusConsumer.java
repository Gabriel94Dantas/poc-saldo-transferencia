package com.example.PocSaldoTransferencia.transferenciaStatus.consumers;

import java.time.Duration;
import java.util.Arrays;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.PocSaldoTransferencia.transferenciaStatus.dtos.TransferenciaDto;
import com.example.PocSaldoTransferencia.transferenciaStatus.utils.JsonUtil;
import com.example.PocSaldoTransferencia.transferenciaStatus.contexts.KafkaTransferenciaStatusContext;
import com.example.PocSaldoTransferencia.transferenciaStatus.services.TransferenciaTranferenciaStatusService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventTransferenciaStatusConsumer {
    
    private Consumer<String, String> consumer;

    @Autowired
    private KafkaTransferenciaStatusContext kafkaContext;

    @Autowired
    private TransferenciaTranferenciaStatusService tranferenciaStatusService;


    public EventTransferenciaStatusConsumer(KafkaTransferenciaStatusContext kafkaTransferenciaStatusContext, 
        TransferenciaTranferenciaStatusService tranferenciaStatusService){
        this.kafkaContext = kafkaTransferenciaStatusContext;
        this.tranferenciaStatusService = tranferenciaStatusService;
        this.consumer = new KafkaConsumer<String, String> (this.kafkaContext.kafkaConsumerProperties());
        this.consumer.subscribe(Arrays.asList("topic-processado-bacen"));
    }

    @Async
    public void getMessages(){
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                Thread.sleep(20000);
                records.forEach(r -> {
                    JsonUtil jsonUtil = new JsonUtil();
                    TransferenciaDto transferenciaDto = jsonUtil.toObject(r.value(), TransferenciaDto.class); 
                    tranferenciaStatusService.mudancaStatus(transferenciaDto);
                });
            }
        } catch (Exception e) {
            log.error("Error to consume data from Kafka", e);
        }finally{
            consumer.close();
        }
    }
}
