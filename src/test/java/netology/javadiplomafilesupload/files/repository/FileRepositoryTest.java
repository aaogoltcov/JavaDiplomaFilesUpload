package netology.javadiplomafilesupload.files.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import netology.javadiplomafilesupload.users.repository.Role;
import netology.javadiplomafilesupload.users.repository.UserEntity;
import netology.javadiplomafilesupload.users.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class FileRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    void setUp() {
        fileRepository.deleteAll();

        FileEntity textFile = new FileEntity();

        textFile.setFileName("test.txt");
        textFile.setFileData("This is a test file.".getBytes());
        textFile.setEditedAt(LocalDateTime.now());
        textFile.setSize(0);

        fileRepository.save(textFile);

        FileEntity docFile = new FileEntity();

        docFile.setFileName("test.doc");
        docFile.setFileData("This is a test file.".getBytes());
        docFile.setEditedAt(LocalDateTime.now());
        docFile.setSize(0);

        fileRepository.save(docFile);
    }

    @Test
    void shouldFindAllWithLimit() {
        assertThat(fileRepository.findAllWithLimit(1)).hasSize(1);
    }

    @Test
    void shouldGetFirstByFileName() {
        assertThat(fileRepository.getFirstByFileName("test.txt")).isNotEmpty();
    }
}
