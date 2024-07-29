package netology.javadiplomafilesupload.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import netology.javadiplomafilesupload.auth.dto.JwtAuthenticationResponse;
import netology.javadiplomafilesupload.auth.dto.SignInRequest;
import netology.javadiplomafilesupload.auth.dto.SignUpRequest;
import netology.javadiplomafilesupload.users.repository.Role;
import netology.javadiplomafilesupload.users.repository.UserEntity;
import netology.javadiplomafilesupload.users.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = UserEntity.builder()
                .username(request.getLogin())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getLogin());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
