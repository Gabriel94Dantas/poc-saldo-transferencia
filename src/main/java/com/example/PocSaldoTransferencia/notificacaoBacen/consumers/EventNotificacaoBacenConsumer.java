package com.example.PocSaldoTransferencia.notificacaoBacen.consumers;

import java.time.Duration;
import java.util.Arrays;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.PocSaldoTransferencia.notificacaoBacen.contexts.KafkaNotificacaoBacenContext;
import com.example.PocSaldoTransferencia.notificacaoBacen.dtos.TransferenciaDto;
import com.example.PocSaldoTransferencia.notificacaoBacen.producers.EventProducer;
import com.example.PocSaldoTransferencia.notificacaoBacen.services.TransferenciaNotificacaoBacenService;
import com.example.PocSaldoTransferencia.notificacaoBacen.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventNotificacaoBacenConsumer {

    private Consumer<String, String> consumer;
    private EventProducer eventProducer;

    @Autowired
    private TransferenciaNotificacaoBacenService transferenciaService;

    @Autowired
    private KafkaNotificacaoBacenContext kafkaNotificacaoBacenContext;
    
    public EventNotificacaoBacenConsumer(KafkaNotificacaoBacenContext kafkaContext, 
         TransferenciaNotificacaoBacenService transferenciaService){
        this.kafkaNotificacaoBacenContext = kafkaContext;
        this.consumer = new KafkaConsumer<String, String> (kafkaNotificacaoBacenContext.kafkaConsumerProperties());
        this.consumer.subscribe(Arrays.asList("topic-notificacao-bacen"));

        this.eventProducer = new EventProducer(kafkaNotificacaoBacenContext);
        this.transferenciaService = transferenciaService;
    }

    @Async
    public void getMessages(){
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                records.forEach(r -> {
                    JsonUtil jsonUtil = new JsonUtil();
                    TransferenciaDto transferenciaDto = jsonUtil.toObject(r.value(), TransferenciaDto.class); 
                    try {
                        transferenciaService.notificarBacen(transferenciaDto);
                    } catch (InterruptedException e) {
                        log.error("Thread sleep error", e);
                    }
                    transferenciaDto.setStatus("Processado pelo BACEN");
                    eventProducer.send(transferenciaDto);
                });
            }
        } catch (Exception e) {
            log.error("Error to consume data from Kafka", e);
        }finally{
            consumer.close();
        }
    }

}
