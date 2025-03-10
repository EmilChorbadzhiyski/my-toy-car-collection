package app.transaction.repository;

import app.transaction.model.Transaction;
import app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByOwnerOrSenderOrReceiver(User owner, User sender, User receiver);
}
