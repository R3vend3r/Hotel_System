package com.infy.consumer.config;

import com.infy.consumer.service.TransferConsumerService;
import com.infy.dto.TransferMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class TransferListener {

    private final TransferConsumerService transferService;

    @KafkaListener(
            topics = "transfers",
            groupId = "bank-group"
    )
    public void listen(TransferMessageDTO event) {

        log.info("Start processing {}", event.getId());

        transferService.process(event);
    }
}