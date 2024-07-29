package netology.javadiplomafilesupload.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import netology.javadiplomafilesupload.auth.dto.JwtAuthenticationResponse;
import netology.javadiplomafilesupload.auth.dto.SignInRequest;
import netology.javadiplomafilesupload.auth.dto.SignUpRequest;
import netology.javadiplomafilesupload.auth.service.AuthenticationService;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}