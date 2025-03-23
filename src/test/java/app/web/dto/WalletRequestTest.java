package app.web.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WalletRequestTest {

    @Test
    void createWalletRequest_shouldSetAllFieldsCorrectly() {
        String owner = "emil";
        BigDecimal balance = new BigDecimal("1000.00");
        LocalDateTime createdOn = LocalDateTime.now();
        LocalDateTime updatedOn = LocalDateTime.now().plusHours(1);

        WalletRequest request = new WalletRequest();
        request.setOwner(owner);
        request.setBalance(balance);
        request.setCreatedOn(createdOn);
        request.setUpdatedOn(updatedOn);

        assertEquals(owner, request.getOwner());
        assertEquals(balance, request.getBalance());
        assertEquals(createdOn, request.getCreatedOn());
        assertEquals(updatedOn, request.getUpdatedOn());
    }

    @Test
    void createWalletRequest_withNullFields_shouldHaveNullFields() {
        WalletRequest request = new WalletRequest();

        assertEquals(null, request.getOwner());
        assertEquals(null, request.getBalance());
        assertEquals(null, request.getCreatedOn());
        assertEquals(null, request.getUpdatedOn());
    }

    @Test
    void setFields_usingSetterMethods_shouldSetFieldsCorrectly() {
        WalletRequest request = new WalletRequest();

        String owner = "emil";
        BigDecimal balance = new BigDecimal("2000.50");
        LocalDateTime createdOn = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedOn = LocalDateTime.now().plusDays(1);

        request.setOwner(owner);
        request.setBalance(balance);
        request.setCreatedOn(createdOn);
        request.setUpdatedOn(updatedOn);

        assertEquals(owner, request.getOwner());
        assertEquals(balance, request.getBalance());
        assertEquals(createdOn, request.getCreatedOn());
        assertEquals(updatedOn, request.getUpdatedOn());
    }

    @Test
    void getFields_usingGetterMethods_shouldRetrieveFieldsCorrectly() {
        String owner = "emil";
        BigDecimal balance = new BigDecimal("500.75");
        LocalDateTime createdOn = LocalDateTime.now().minusHours(2);
        LocalDateTime updatedOn = LocalDateTime.now().plusMinutes(30);

        WalletRequest request = new WalletRequest();
        request.setOwner(owner);
        request.setBalance(balance);
        request.setCreatedOn(createdOn);
        request.setUpdatedOn(updatedOn);

        assertEquals(owner, request.getOwner());
        assertEquals(balance, request.getBalance());
        assertEquals(createdOn, request.getCreatedOn());
        assertEquals(updatedOn, request.getUpdatedOn());
    }
}