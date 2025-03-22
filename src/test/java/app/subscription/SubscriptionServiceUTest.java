package app.subscription;

import app.subscription.model.Subscription;
import app.subscription.model.SubscriptionPeriod;
import app.subscription.model.SubscriptionType;
import app.subscription.repository.SubscriptionRepository;
import app.subscription.service.SubscriptionService;
import app.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceUTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private User user;

    @Test
    void shouldHandleExceptionWhenSavingSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenThrow(new RuntimeException("Error saving subscription"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> subscriptionService.createDefaultSubscription(user));
        assertEquals("Error saving subscription", exception.getMessage());
    }

    @Test
    void shouldInitializeSubscriptionWithCorrectValues() {
        LocalDateTime now = LocalDateTime.now();

        Subscription savedSubscription = Subscription.builder()
                .id(UUID.randomUUID())
                .owner(user)
                .type(SubscriptionType.FREE)
                .period(SubscriptionPeriod.MONTHLY)
                .price(new BigDecimal("0.00"))
                .createdOn(now)
                .build();

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(savedSubscription);
        subscriptionService.createDefaultSubscription(user);
        verify(subscriptionRepository, times(1)).save(argThat(subscription ->
                subscription.getOwner().equals(user) &&
                        subscription.getType().equals(SubscriptionType.FREE) &&
                        subscription.getPeriod().equals(SubscriptionPeriod.MONTHLY) &&
                        subscription.getPrice().equals(new BigDecimal("0.00")) &&
                        subscription.getCreatedOn() != null
        ));
    }
}
