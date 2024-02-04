package com.example.PocSaldoTransferencia.notificacaoBacen.producers;

import java.io.Closeable;
import java.io.IOException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;

import com.example.PocSaldoTransferencia.notificacaoBacen.contexts.KafkaNotificacaoBacenContext;
import com.example.PocSaldoTransferencia.notificacaoBacen.dtos.TransferenciaDto;
import com.example.PocSaldoTransferencia.notificacaoBacen.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventProducer implements Closeable {
        private final KafkaProducer<String, String> kafkaProducer;

    @Value("${MY_TOPIC}")
    private String myTopic;

    public EventProducer(KafkaNotificacaoBacenContext kafkaContext){
        this.kafkaProducer = new KafkaProducer<String, String>(kafkaContext.kafkaProducerProperties());
    }

    public void send(TransferenciaDto transferenciaDto){
        try {
            JsonUtil jsonUtil = new JsonUtil();
            ProducerRecord<String, String> record = new ProducerRecord<String,String>("topic-processado-bacen", 
                transferenciaDto.getId().toString(), jsonUtil.toJson(transferenciaDto));
            
            kafkaProducer.send(record, (recordData,e) -> {
                if (e != null){
                    log.error("Error to send data to Kafka", e);
                    throw new RuntimeException(e);
                }else{
                    log.info("Message key = " + record.key());
                    log.info("Message value = " + record.value());
                    log.info("Message offset = " + recordData.offset());
                }
            });
        } catch (Exception e) {
            log.error("Error to send data to Kafka", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException{
        this.kafkaProducer.close();
    }
}
