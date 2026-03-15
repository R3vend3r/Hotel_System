package com.infy.producer.service;

import com.infy.entity.Account;
import com.infy.repo.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    private final Map<UUID, Account> accountMap = new HashMap<>();

    @PostConstruct
    public void initAccounts() {

        if (accountRepository.count() == 0) {

            log.info("Creating 1000 accounts...");

            for (int i = 1; i <= 1000; i++) {

                Account account = new Account();
                account.setId(UUID.randomUUID()); // Генерация случайного UUID
                account.setBalance(BigDecimal.valueOf(10000));

                accountRepository.save(account);
                accountMap.put(account.getId(), account);
            }

        } else {

            log.info("Loading accounts from DB...");

            accountRepository.findAll()
                    .forEach(acc -> accountMap.put(acc.getId(), acc));
        }

        log.info("Accounts loaded: {}", accountMap.size());
    }

    public UUID getRandomAccountId() {

        List<UUID> ids = new ArrayList<>(accountMap.keySet());

        Random random = new Random();

        return ids.get(random.nextInt(ids.size()));
    }
}