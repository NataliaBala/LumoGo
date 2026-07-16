package pl.lumogo.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lumogo.backend.dto.AuthResponse;
import pl.lumogo.backend.dto.LoginRequest;
import pl.lumogo.backend.dto.RegisterRequest;
import pl.lumogo.backend.model.AppUser;
import pl.lumogo.backend.repository.AppUserRepository;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(AppUserRepository appUserRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        validateRegistration(request);

        if (appUserRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("error", "Konto z tym adresem e-mail już istnieje");
        }

        AppUser appUser = new AppUser(
            request.getFirstName().trim(),
            request.getLastName().trim(),
            request.getEmail().trim().toLowerCase(),
            passwordEncoder.encode(request.getPassword())
        );

        appUserRepository.save(appUser);

        return new AuthResponse(
            "success",
            "Konto utworzone pomyślnie",
            appUser.getEmail(),
            appUser.getFirstName(),
            appUser.getLastName()
        );
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank() || request.getPassword() == null) {
            return new AuthResponse("error", "E-mail i hasło są wymagane");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            request.getEmail().trim().toLowerCase(),
            request.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthResponse(
            "success",
            "Zalogowano pomyślnie",
            request.getEmail().trim().toLowerCase(),
            null,
            null
        );
    }

    private void validateRegistration(RegisterRequest request) {
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            throw new IllegalArgumentException("Imię jest wymagane");
        }
        if (request.getLastName() == null || request.getLastName().isBlank()) {
            throw new IllegalArgumentException("Nazwisko jest wymagane");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("E-mail jest wymagany");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Hasło jest wymagane");
        }
        if (request.getPasswordConfirm() == null || request.getPasswordConfirm().isBlank()) {
            throw new IllegalArgumentException("Potwierdzenie hasła jest wymagane");
        }
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("Hasła muszą być takie same");
        }
        if (request.getPassword().length() < 5) {
            throw new IllegalArgumentException("Hasło musi mieć co najmniej 5 znaków");
        }
        String password = request.getPassword();
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("Hasło musi zawierać co najmniej jedną dużą literę, jedną małą literę i jedną cyfrę");
        }
    }
}
