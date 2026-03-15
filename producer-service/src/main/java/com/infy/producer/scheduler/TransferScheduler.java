package com.infy.producer.scheduler;

import com.infy.producer.service.AccountService;
import com.infy.dto.TransferMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferScheduler {

    private final KafkaTemplate<String, TransferMessageDTO> kafkaTemplate;
    private final AccountService accountService;

    @Scheduled(fixedDelay = 200)
    public void generateTransfer() {

        UUID from = accountService.getRandomAccountId();
        UUID to = accountService.getRandomAccountId();

        TransferMessageDTO event = new TransferMessageDTO();

        event.setId(UUID.randomUUID());
        event.setFromAccountId(from);
        event.setToAccountId(to);
        event.setAmount(BigDecimal.valueOf(new Random().nextInt(100)));

        kafkaTemplate.send("transfers", event);

        log.info("Sent transfer {}", event.getId());
    }
}