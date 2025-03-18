package com.hsbc.demo.service;

import com.hsbc.demo.entity.Transaction;
import com.hsbc.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    private TransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void createTransaction() {
        String accountId = "123";
        BigDecimal amount = BigDecimal.TEN;
        String id = UUID.randomUUID().toString();
        LocalDateTime timestamp = LocalDateTime.now();
        Transaction transaction = new Transaction(id, accountId, amount, timestamp);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.createTransaction(accountId, amount);

        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAccountId(), result.getAccountId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getTimestamp(), result.getTimestamp());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void getTransaction() {
        String id = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(id, "123", BigDecimal.TEN, LocalDateTime.now());

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        Optional<Transaction> result = transactionService.getTransaction(id);

        assertTrue(result.isPresent());
        assertEquals(transaction.getId(), result.get().getId());
        assertEquals(transaction.getAccountId(), result.get().getAccountId());
        assertEquals(transaction.getAmount(), result.get().getAmount());
        assertEquals(transaction.getTimestamp(), result.get().getTimestamp());
        verify(transactionRepository, times(1)).findById(id);
    }

    @Test
    void getAllTransactions() {
        Transaction transaction1 = new Transaction(UUID.randomUUID().toString(), "123", BigDecimal.TEN, LocalDateTime.now());
        Transaction transaction2 = new Transaction(UUID.randomUUID().toString(), "456", BigDecimal.ONE, LocalDateTime.now());
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();

        assertEquals(transactions.size(), result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void deleteTransaction() {
        String id = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(id, "123", BigDecimal.TEN, LocalDateTime.now());

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(id);

        verify(transactionRepository, times(1)).deleteById(id);
    }
}    