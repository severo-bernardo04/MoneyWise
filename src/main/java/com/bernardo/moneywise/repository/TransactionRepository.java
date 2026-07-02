package com.bernardo.moneywise.repository;

import com.bernardo.moneywise.enums.TransactionType;
import com.bernardo.moneywise.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByCategory_NameContainingIgnoreCase(String categoryName, Pageable pageable);

    Page<Transaction> findByType(TransactionType type, Pageable pageable);

}
