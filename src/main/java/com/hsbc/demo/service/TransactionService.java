package com.hsbc.demo.service;

import com.hsbc.demo.entity.Transaction;
import com.hsbc.demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class TransactionService {
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(String accountId, BigDecimal amount) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be a positive value");
        }
        String id = UUID.randomUUID().toString();
        LocalDateTime timestamp = LocalDateTime.now();
        Transaction transaction = new Transaction(id, accountId, amount, timestamp);
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransaction(String id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid transaction ID format");
        }
        return transactionRepository.findById(id);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByPage(int page, int size) {
        List<Transaction> allTransactions = transactionRepository.findAll();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allTransactions.size());
        if (startIndex >= allTransactions.size()) {
            return Collections.emptyList();
        }
        return allTransactions.subList(startIndex, endIndex);
    }

    public void deleteTransaction(String id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid transaction ID format");
        }
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new RuntimeException("Transaction not found for deletion");
        }
        transactionRepository.deleteById(id);
    }

    private boolean isValidId(String id) {
        return UUID_PATTERN.matcher(id).matches();
    }
}    