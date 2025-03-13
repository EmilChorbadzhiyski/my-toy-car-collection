package app.web.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateNewPost {

    @NotBlank(message = "This field must not be empty!")
    private String title;

    @NotBlank(message = "This field must not be empty!")
    @Size(max = 1000)
    private String description;

}
