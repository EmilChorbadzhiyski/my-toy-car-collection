package app.transaction.service;

import app.transaction.model.Transaction;
import app.transaction.repository.TransactionRepository;
import app.user.model.User;
import app.web.dto.CreateTransactionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public void createNewTransaction(CreateTransactionRequest createTransaction, User user){

        Transaction transaction = Transaction.builder()
                .owner(user)
                .sender(createTransaction.getSender())
                .receiver(createTransaction.getReceiver())
                .amount(createTransaction.getAmount())
                .createdOn(createTransaction.getCreatedOn())
                .build();

        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsForUser(User user) {
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getOwner().equals(user))
                .toList();
    }

    public void deleteTransaction(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() ->
                new IllegalArgumentException("Transaction not found with id: " + transactionId));
        transactionRepository.delete(transaction);
    }

}
