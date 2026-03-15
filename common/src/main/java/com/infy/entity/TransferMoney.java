package com.infy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "transfers")
public class TransferMoney {
    @Id
    private UUID id;
    @Column(name = "to_account_id")
    private UUID toAccountId;
    @Column(name = "from_account_id")
    private UUID fromAccountId;
    private BigDecimal amount;
    private String status;

}
