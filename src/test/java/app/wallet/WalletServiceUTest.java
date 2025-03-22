package app.wallet;

import app.exeptions.DomainException;
import app.user.service.UserService;
import app.wallet.model.Wallet;
import app.user.model.User;
import app.wallet.repository.WalletRepository;
import app.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceUTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private WalletService walletService;

    private User user;
    private Wallet existingWallet;
    private Wallet newWallet;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        existingWallet = mock(Wallet.class);
        newWallet = mock(Wallet.class);
        wallet = mock(Wallet.class);
    }


    @Test
    void shouldThrowExceptionWhenWalletAlreadyExists() {
        when(walletRepository.findByOwner(user)).thenReturn(Optional.of(existingWallet));
        DomainException exception = assertThrows(DomainException.class, () -> walletService.createNewWallet(user));
        assertEquals("Wallet already exists for the user.", exception.getMessage());
        verify(walletRepository, times(0)).save(any(Wallet.class));
    }

    @Test
    void shouldCreateNewWalletWhenNoneExists() {
        when(walletRepository.findByOwner(user)).thenReturn(Optional.empty());
        when(walletRepository.save(any(Wallet.class))).thenReturn(newWallet);
        Wallet createdWallet = walletService.createNewWallet(user);
        assertNotNull(createdWallet);
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void shouldCreateWalletCorrectly() {
        Wallet wallet = walletService.initializeWallet(user);
        assertNotNull(wallet);
        assertEquals(user, wallet.getOwner());
        assertEquals(new BigDecimal("30.00"), wallet.getBalance());
        assertNotNull(wallet.getCreatedOn());
        assertNotNull(wallet.getUpdatedOn());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        BigDecimal initialBalance = new BigDecimal("10.00");
        BigDecimal carPrice = new BigDecimal("30.00");
        when(existingWallet.getBalance()).thenReturn(initialBalance);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> walletService.decreaseBalance(existingWallet, carPrice));
        assertEquals("Insufficient funds", exception.getMessage());
        verify(walletRepository, times(0)).save(existingWallet);
    }
    @Test
    void shouldDecreaseBalance_WhenSufficientFunds() {
        BigDecimal initialBalance = new BigDecimal("100.00");
        BigDecimal carPrice = new BigDecimal("30.00");
        when(existingWallet.getBalance()).thenReturn(initialBalance);
        walletService.decreaseBalance(existingWallet, carPrice);
        verify(existingWallet, times(1)).setBalance(new BigDecimal("70.00"));
        verify(walletRepository, times(1)).save(existingWallet);
    }

    @Test
    void shouldIncreaseBalance_WhenAmountIsAdded() {
        BigDecimal initialBalance = new BigDecimal("50.00");
        BigDecimal amountToAdd = new BigDecimal("30.00");
        BigDecimal expectedBalance = new BigDecimal("80.00");
        when(existingWallet.getBalance()).thenReturn(initialBalance);
        walletService.increaseBalance(existingWallet, amountToAdd);
        verify(existingWallet, times(1)).setBalance(expectedBalance);
        verify(walletRepository, times(1)).save(existingWallet);
    }

    @Test
    void shouldReturnWallet_WhenExistsForUser() {
        User user = mock(User.class);
        Wallet expectedWallet = mock(Wallet.class);
        when(walletRepository.findByOwner(user)).thenReturn(Optional.of(expectedWallet));
        Optional<Wallet> actualWallet = walletService.getWalletByUser(user);
        assertTrue(actualWallet.isPresent());
        assertEquals(expectedWallet, actualWallet.get());
        verify(walletRepository, times(1)).findByOwner(user);
    }

    @Test
    void shouldAddMoneyToWallet() {
        BigDecimal initialBalance = new BigDecimal("50.00");
        BigDecimal amountToAdd = new BigDecimal("30.00");
        BigDecimal expectedBalance = new BigDecimal("80.00");
        when(wallet.getBalance()).thenReturn(initialBalance);
        walletService.addMoney(wallet, amountToAdd);
        verify(wallet, times(1)).setBalance(expectedBalance);
        verify(wallet, times(1)).setUpdatedOn(any(LocalDateTime.class));
        verify(walletRepository, times(1)).save(wallet);
    }
}
