package com.infy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Account {
    @Id
    private UUID id;
    private BigDecimal balance;
}
