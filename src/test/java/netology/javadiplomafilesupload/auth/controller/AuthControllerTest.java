package netology.javadiplomafilesupload.auth.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import netology.javadiplomafilesupload.users.repository.Role;
import netology.javadiplomafilesupload.users.repository.UserEntity;
import netology.javadiplomafilesupload.users.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AuthControllerTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");
    @LocalServerPort
    private Integer port;
    @Autowired
    private UserRepository userRepository;

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

        given()
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
                .post("/sign-up");
    }

    @Test
    void shouldSignUp() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .body("""
                        {
                            "login": "user_",
                            "email": "user_@user.ru",
                            "password": "user_"
                        }
                        """)
                .when()
                .post("/sign-up")
                .then()
                .statusCode(200)
                .body("auth-token", is(notNullValue()));
    }

    @Test
    void shouldSignIn() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "login": "user",
                            "password": "user"
                        }
                        """)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("auth-token", is(notNullValue()));
    }
}
