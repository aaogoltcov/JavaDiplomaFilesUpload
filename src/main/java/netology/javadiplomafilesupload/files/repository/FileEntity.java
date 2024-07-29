package netology.javadiplomafilesupload.files.repository;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "files")
@Getter
@Setter
@Entity
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_id_seq")
    @SequenceGenerator(name = "file_id_seq", sequenceName = "file_id_seq", allocationSize = 1)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    @JsonProperty(value = "filename")
    private String fileName;

    @Column(unique = true, nullable = false)
    private byte[] fileData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime editedAt;

    @Column(nullable = false)
    private double size;
}
