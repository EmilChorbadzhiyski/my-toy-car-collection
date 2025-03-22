package app.transaction;

import app.transaction.model.Transaction;
import app.transaction.repository.TransactionRepository;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.web.dto.CreateTransactionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceUTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldCreateNewTransaction() {
        User user = mock(User.class);
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .sender("Alice")
                .receiver("Bob")
                .amount(new BigDecimal("100.00"))
                .createdOn(LocalDateTime.now())
                .build();

        transactionService.createNewTransaction(request, user);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldReturnTransactionsForUser() {
        User user = mock(User.class);
        Transaction transaction1 = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);

        when(transaction1.getOwner()).thenReturn(user);
        when(transaction2.getOwner()).thenReturn(user);
        when(transaction3.getOwner()).thenReturn(mock(User.class));
        List<Transaction> allTransactions = List.of(transaction1, transaction2, transaction3);
        when(transactionRepository.findAll()).thenReturn(allTransactions);
        List<Transaction> result = transactionService.getTransactionsForUser(user);
        assertEquals(2, result.size());
        assertEquals(transaction1, result.get(0));
        assertEquals(transaction2, result.get(1));
        verify(transactionRepository, times(1)).findAll();
    }
    @Test
    void shouldDeleteTransaction() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = mock(Transaction.class);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        transactionService.deleteTransaction(transactionId);
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.deleteTransaction(transactionId));
        assertEquals("Transaction not found with id: " + transactionId, exception.getMessage());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(0)).delete(any(Transaction.class));
    }

}
