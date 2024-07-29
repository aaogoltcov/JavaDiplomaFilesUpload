package netology.javadiplomafilesupload.files.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static io.restassured.RestAssured.given;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import netology.javadiplomafilesupload.files.repository.FileEntity;
import netology.javadiplomafilesupload.files.repository.FileRepository;
import netology.javadiplomafilesupload.users.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class FileControllerTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");
    @LocalServerPort
    private Integer port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;
    private String token;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("jwt.token.secret", () -> "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855");
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
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

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .body("""
                        {
                            "login": "user",
                            "email": "user@user.ru",
                            "password": "user"
                        }
                        """)
                .when()
                .post("/sign-up").then().extract().response();

        token = response.jsonPath().getString("auth-token");
    }

    @Test
    void shouldGetFilesWithLimit() {
        given()
                .headers(
                        "Auth-Token",
                        "Bearer " + token
                )
                .contentType(ContentType.JSON)
                .when()
                .queryParam("limit", "1")
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .body("", hasSize(1));
    }

    @Test
    void shouldGetFile() {
        given()
                .headers(
                        "Auth-Token",
                        "Bearer " + token
                )
                .contentType(ContentType.JSON)
                .when()
                .queryParam("filename", "test.txt")
                .when()
                .get("/file")
                .then()
                .statusCode(200)
                .body("", is(notNullValue()));
    }

    @Test
    void shouldGetFileByFileName() {
        given()
                .headers(
                        "Auth-Token",
                        "Bearer " + token
                )
                .contentType(ContentType.JSON)
                .when()
                .queryParam("filename", "test.txt")
                .when()
                .get("/file")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldDeleteFileByFileName() {
        given()
                .headers(
                        "Auth-Token",
                        "Bearer " + token
                )
                .contentType(ContentType.JSON)
                .when()
                .queryParam("filename", "test.txt")
                .when()
                .delete("/file")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldUploadFile() {
        given()
                .headers(
                        "Auth-Token",
                        "Bearer " + token
                )
                .contentType(ContentType.MULTIPART)
                .queryParam("filename", "test-upload.txt")
                .multiPart("file", "test-upload", "This is a test file.".getBytes())
                .when()
                .post("/file")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldUpdateFileByFileName() {
        given()
                .headers(
                        "Auth-Token",
                        "Bearer " + token
                )
                .contentType(ContentType.JSON)
                .when()
                .queryParam("filename", "test.txt")
                .body("""
                        { "filename": "test-update.txt"}
                        """)
                .when()
                .put("/file")
                .then()
                .statusCode(200);
    }
}
