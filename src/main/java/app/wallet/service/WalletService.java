package app.wallet.service;

import app.exeptions.DomainException;
import app.user.model.User;
import app.wallet.model.Wallet;
import app.wallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createNewWallet(User user) {
        Optional<Wallet> existingWallet = walletRepository.findByOwner(user);
        if (existingWallet.isPresent()) {
            throw new DomainException("Wallet already exists for the user.");
        }
        Wallet wallet = walletRepository.save(initializeWallet(user));
//        log.info("Successfully create new wallet with id [%s] and balance [%.2f].".formatted(wallet.getId(), wallet.getBalance()));
        return wallet;
    }

    private Wallet initializeWallet(User user) {
        return Wallet.builder()
                .owner(user)
                .balance(new BigDecimal("30.00"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now()).build();
    }

    public void decreaseBalance(Wallet wallet, BigDecimal carPrice) {
        if (wallet.getBalance().compareTo(carPrice) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        wallet.setBalance(wallet.getBalance().subtract(carPrice));
        wallet.setUpdatedOn(LocalDateTime.now());
        walletRepository.save(wallet);
    }

    public void increaseBalance(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedOn(LocalDateTime.now());
        walletRepository.save(wallet);
    }

    public Optional<Wallet> getWalletByUser(User user) {
        return walletRepository.findByOwner(user);
    }

    public void addMoney(Wallet wallet, BigDecimal amount) {
        BigDecimal currentBalance = wallet.getBalance();
        wallet.setBalance(currentBalance.add(amount));
        wallet.setUpdatedOn(LocalDateTime.now());
        walletRepository.save(wallet);
    }
}
