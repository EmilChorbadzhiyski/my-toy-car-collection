package app.web.dto;

import app.car.model.CarOrigin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCarRequest {


    private UUID ownerId;

    @NotBlank(message = "Please enter type!")
    private String type;

    @NotBlank(message = "Please enter brand!")
    private String brand;

    @NotBlank(message = "Please enter model!")
    private String model;

    @NotNull(message = "Please enter year!")
    private String year;

    @NotNull(message = "Please enter number of doors!")
    @Min(1)
    private int numberOfDoors;

    @NotNull
    private CarOrigin countryOfOrigin;

    @NotNull(message = "Please enter color!")
    private String color;

    @NotNull(message = "Please enter URL!")
    private String pictureUrl;

    @NotNull(message = "Please enter price!")
    private BigDecimal price;

}
