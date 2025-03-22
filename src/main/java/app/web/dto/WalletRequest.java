package app.web.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletRequest {
//Todo
    private String owner;
    private BigDecimal balance;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
