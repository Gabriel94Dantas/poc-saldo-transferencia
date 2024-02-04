package com.example.PocSaldoTransferencia.producers;

import java.io.Closeable;
import java.io.IOException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;

import com.example.PocSaldoTransferencia.contexts.KafkaContext;
import com.example.PocSaldoTransferencia.entities.Transferencia;
import com.example.PocSaldoTransferencia.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventProducer implements Closeable {

    private final KafkaProducer<String, String> kafkaProducer;

    @Value("${MY_TOPIC}")
    private String myTopic;

    public EventProducer(KafkaContext kafkaContext){
        this.kafkaProducer = new KafkaProducer<String, String>(kafkaContext.kafkaProperties());
    }

    public void send(Transferencia transferencia){
        try {
            JsonUtil jsonUtil = new JsonUtil();
            ProducerRecord<String, String> record = new ProducerRecord<String,String>("topic-default", 
                transferencia.getId().toString(), jsonUtil.toJson(transferencia));
            
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
