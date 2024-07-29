package netology.javadiplomafilesupload.files.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileResponse {
    @NotBlank()
    @JsonProperty(value = "filename")
    private String fileName;

    @NotBlank()
    private LocalDateTime editedAt;

    @NotBlank()
    private double size;
}
