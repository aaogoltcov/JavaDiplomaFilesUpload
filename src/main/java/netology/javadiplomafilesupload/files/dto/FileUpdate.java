package netology.javadiplomafilesupload.files.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileUpdate {
    @NotBlank()
    @JsonProperty(value = "filename")
    private String fileName;
}
