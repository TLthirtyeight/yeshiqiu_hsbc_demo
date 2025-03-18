package com.hsbc.demo.repository;

import com.hsbc.demo.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {
    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    public Transaction save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    public void deleteById(String id) {
        transactions.remove(id);
    }
}    