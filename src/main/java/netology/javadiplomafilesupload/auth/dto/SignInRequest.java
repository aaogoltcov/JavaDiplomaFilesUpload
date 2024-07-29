package netology.javadiplomafilesupload.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequest {
    @Size(min = 4, max = 50)
    @NotBlank()
    private String login;

    @Size(min = 4, max = 255)
    @NotBlank()
    private String password;
}
