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
import pl.lumogo.backend.dto.UpdateProfileRequest;
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

        String email = request.getEmail().trim().toLowerCase();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            email,
            request.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser appUser = appUserRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));

        return new AuthResponse(
            "success",
            "Zalogowano pomyślnie",
            appUser.getEmail(),
            appUser.getFirstName(),
            appUser.getLastName(),
            appUser.getInterests()
        );
    }

    public AuthResponse getProfile(String email) {
        if (email == null || email.isBlank()) {
            return new AuthResponse("error", "E-mail jest wymagany");
        }

        AppUser appUser = appUserRepository.findByEmail(email.trim().toLowerCase())
            .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));

        return new AuthResponse(
            "success",
            "Profil pobrany pomyślnie",
            appUser.getEmail(),
            appUser.getFirstName(),
            appUser.getLastName(),
            appUser.getInterests()
        );
    }

    @Transactional
    public AuthResponse updateProfile(UpdateProfileRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return new AuthResponse("error", "E-mail jest wymagany");
        }

        String email = request.getEmail().trim().toLowerCase();
        AppUser appUser = appUserRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));

        boolean updated = false;

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            appUser.setFirstName(request.getFirstName().trim());
            updated = true;
        }

        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            appUser.setLastName(request.getLastName().trim());
            updated = true;
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
                throw new IllegalArgumentException("Aktualne hasło jest wymagane do zmiany hasła");
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), appUser.getPassword())) {
                throw new IllegalArgumentException("Aktualne hasło jest nieprawidłowe");
            }
            if (request.getPasswordConfirm() == null || !request.getPassword().equals(request.getPasswordConfirm())) {
                throw new IllegalArgumentException("Hasła muszą być takie same");
            }
            validatePassword(request.getPassword());
            appUser.setPassword(passwordEncoder.encode(request.getPassword()));
            updated = true;
        }

        if (!updated) {
            return new AuthResponse("error", "Brak zmian do zapisania");
        }

        appUserRepository.save(appUser);

        return new AuthResponse(
            "success",
            "Profil zaktualizowany pomyślnie",
            appUser.getEmail(),
            appUser.getFirstName(),
            appUser.getLastName(),
            appUser.getInterests()
        );
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Hasło jest wymagane");
        }
        if (password.length() < 5) {
            throw new IllegalArgumentException("Hasło musi mieć co najmniej 5 znaków");
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("Hasło musi zawierać co najmniej jedną dużą literę, jedną małą literę i jedną cyfrę");
        }
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
