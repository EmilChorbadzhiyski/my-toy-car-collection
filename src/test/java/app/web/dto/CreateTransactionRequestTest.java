package app.web.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateTransactionRequestTest {

    @Test
    void buildCreateTransactionRequest_shouldSetAllFieldsCorrectly() {
        UUID ownerId = UUID.randomUUID();
        String sender = "Sender123";
        String receiver = "Receiver456";
        BigDecimal amount = new BigDecimal("100.50");
        LocalDateTime createdOn = LocalDateTime.now();

        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .ownerId(ownerId)
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .createdOn(createdOn)
                .build();

        assertEquals(ownerId, request.getOwnerId());
        assertEquals(sender, request.getSender());
        assertEquals(receiver, request.getReceiver());
        assertEquals(amount, request.getAmount());
        assertEquals(createdOn, request.getCreatedOn());
    }

    @Test
    void createCreateTransactionRequest_withNoArgsConstructor_shouldHaveNullFields() {
        CreateTransactionRequest request = new CreateTransactionRequest();

        assertEquals(null, request.getOwnerId());
        assertEquals(null, request.getSender());
        assertEquals(null, request.getReceiver());
        assertEquals(null, request.getAmount());
        assertEquals(null, request.getCreatedOn());
    }

    @Test
    void createCreateTransactionRequest_withAllArgsConstructor_shouldSetAllFields() {
        UUID ownerId = UUID.randomUUID();
        String sender = "Sender789";
        String receiver = "Receiver101";
        BigDecimal amount = new BigDecimal("50.25");
        LocalDateTime createdOn = LocalDateTime.now().plusHours(1);

        CreateTransactionRequest request = new CreateTransactionRequest(ownerId, sender, receiver, amount, createdOn);

        assertEquals(ownerId, request.getOwnerId());
        assertEquals(sender, request.getSender());
        assertEquals(receiver, request.getReceiver());
        assertEquals(amount, request.getAmount());
        assertEquals(createdOn, request.getCreatedOn());
    }

    @Test
    void createCreateTransactionRequest_withPartialArgsConstructor_shouldSetPartialFields() {
        String sender = "Sender112";
        String receiver = "Receiver131";
        BigDecimal amount = new BigDecimal("75.75");
        LocalDateTime createdOn = LocalDateTime.now().plusMinutes(30);

        CreateTransactionRequest request = new CreateTransactionRequest(sender, receiver, amount, createdOn);

        assertEquals(null, request.getOwnerId());
        assertEquals(sender, request.getSender());
        assertEquals(receiver, request.getReceiver());
        assertEquals(amount, request.getAmount());
        assertEquals(createdOn, request.getCreatedOn());
    }

    @Test
    void setFields_usingSetterMethods_shouldSetFieldsCorrectly() {
        CreateTransactionRequest request = new CreateTransactionRequest();

        UUID ownerId = UUID.randomUUID();
        String sender = "UpdatedSender";
        String receiver = "UpdatedReceiver";
        BigDecimal amount = new BigDecimal("200.00");
        LocalDateTime createdOn = LocalDateTime.now().plusDays(1);

        request.setOwnerId(ownerId);
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setAmount(amount);
        request.setCreatedOn(createdOn);

        assertEquals(ownerId, request.getOwnerId());
        assertEquals(sender, request.getSender());
        assertEquals(receiver, request.getReceiver());
        assertEquals(amount, request.getAmount());
        assertEquals(createdOn, request.getCreatedOn());
    }

    @Test
    void getFields_usingGetterMethods_shouldRetrieveFieldsCorrectly() {
        UUID ownerId = UUID.randomUUID();
        String sender = "RetrievedSender";
        String receiver = "RetrievedReceiver";
        BigDecimal amount = new BigDecimal("150.99");
        LocalDateTime createdOn = LocalDateTime.now().minusHours(2);

        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .ownerId(ownerId)
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .createdOn(createdOn)
                .build();

        assertEquals(ownerId, request.getOwnerId());
        assertEquals(sender, request.getSender());
        assertEquals(receiver, request.getReceiver());
        assertEquals(amount, request.getAmount());
        assertEquals(createdOn, request.getCreatedOn());
    }
}