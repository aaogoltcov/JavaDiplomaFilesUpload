package netology.javadiplomafilesupload.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netology.javadiplomafilesupload.log.LogMarker;
import netology.javadiplomafilesupload.users.repository.UserEntity;
import netology.javadiplomafilesupload.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;

    public UserEntity save(UserEntity user) {
        return repository.save(user);
    }

    public UserEntity create(UserEntity user) {
        if (repository.existsByUsername(user.getUsername())) {
            log.warn(LogMarker.CREATE, "User with username {} already exists", user.getUsername());

            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            log.warn(LogMarker.CREATE, "User with email {} already exists", user.getEmail());

            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    public UserEntity getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(LogMarker.READ, "User with username {} already exists", username);

                    return new UsernameNotFoundException("Пользователь не найден");
                });
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
