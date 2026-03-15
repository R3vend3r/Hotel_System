package com.infy.repo;

import com.infy.entity.TransferMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TransferMoneyRepository extends JpaRepository<TransferMoney, UUID> {
}