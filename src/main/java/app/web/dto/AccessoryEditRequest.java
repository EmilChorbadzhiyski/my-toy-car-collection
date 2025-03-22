package app.web.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessoryEditRequest {
    private UUID id;
    private String accessoryName;
    private String description;
    private String imageUrl;
    private double price;
}
