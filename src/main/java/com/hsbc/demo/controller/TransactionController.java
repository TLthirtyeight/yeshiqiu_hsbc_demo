package com.hsbc.demo.controller;

import com.hsbc.demo.entity.Transaction;
import com.hsbc.demo.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * create transaction
     *
     * @param accountId
     * @param amount
     * @return
     */

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestParam String accountId, @RequestParam BigDecimal amount) {
        try {
            Transaction transaction = transactionService.createTransaction(accountId, amount);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  get transaction by id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String id) {
        Optional<Transaction> transaction = transactionService.getTransaction(id);
        return transaction.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
               .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * get transactions by page
     *
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactionsByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.getTransactionsByPage(page, size);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        try {
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}    