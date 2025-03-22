package app.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccessoryCreateRequest {
    private UUID userId;
    private String accessoryName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
