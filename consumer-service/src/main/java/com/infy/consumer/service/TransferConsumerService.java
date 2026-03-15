package com.infy.consumer.service;

import com.infy.entity.Account;
import com.infy.entity.TransferMoney;
import com.infy.repo.AccountRepository;
import com.infy.repo.TransferMoneyRepository;
import com.infy.dto.TransferMessageDTO;
import com.infy.enums.TransferStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferConsumerService {

    private final AccountRepository accountRepository;
    private final TransferMoneyRepository transferRepository;

    @Transactional
    public void process(TransferMessageDTO event) {

        Account from = accountRepository.findById(event.getFromAccountId()).orElse(null);
        Account to = accountRepository.findById(event.getToAccountId()).orElse(null);

        if (from == null || to == null) {
            log.error("Accounts not found");
            saveTransferWithError(event);
            return;
        }

        if (from.getBalance().compareTo(event.getAmount()) < 0) {
            log.error("Not enough balance");
            saveTransferWithError(event);
            return;
        }

        from.setBalance(from.getBalance().subtract(event.getAmount()));
        to.setBalance(to.getBalance().add(event.getAmount()));

        accountRepository.save(from);
        accountRepository.save(to);

        TransferMoney transfer = new TransferMoney();
        transfer.setId(event.getId());
        transfer.setFromAccountId(event.getFromAccountId());
        transfer.setToAccountId(event.getToAccountId());
        transfer.setAmount(event.getAmount());
        transfer.setStatus(TransferStatus.SUCCESS.getValue());

        transferRepository.save(transfer);
    }


    private void saveTransferWithError(TransferMessageDTO event) {
        TransferMoney transfer = new TransferMoney();
        transfer.setId(event.getId());
        transfer.setFromAccountId(event.getFromAccountId());
        transfer.setToAccountId(event.getToAccountId());
        transfer.setAmount(event.getAmount());
        transfer.setStatus(TransferStatus.FAILED.getValue());

        transferRepository.save(transfer);
    }
}