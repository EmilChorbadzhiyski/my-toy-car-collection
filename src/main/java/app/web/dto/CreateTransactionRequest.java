package app.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTransactionRequest {

    @NotNull
    private UUID ownerId;

    @NotNull
    private String sender;

    @NotNull
    private String receiver;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDateTime createdOn;

    public CreateTransactionRequest(String sender, String receiver, BigDecimal amount, LocalDateTime createdOn) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.createdOn = createdOn;
    }
}
