package pl.lumogo.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lumogo.backend.model.AppUser;
import pl.lumogo.backend.repository.AppUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterestsService {

    private final AppUserRepository appUserRepository;
    private final List<String> availableInterests = List.of(
        "Bieganie",
        "Spacer",
        "Trekking",
        "Trucht",
        "Siła",
        "Fitness",
        "Rower",
        "Joga",
        "Nordic walking",
        "Crossfit"
    );

    public InterestsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<String> getAvailableInterests() {
        return availableInterests;
    }

    @Transactional
    public Optional<AppUser> saveUserInterests(String email, List<String> interests) {
        return appUserRepository.findByEmail(email.trim().toLowerCase())
            .map(user -> {
                String selected = interests.stream()
                    .filter(availableInterests::contains)
                    .collect(Collectors.joining(","));
                user.setInterests(selected);
                return appUserRepository.save(user);
            });
    }

    public Optional<List<String>> getUserInterests(String email) {
        return appUserRepository.findByEmail(email.trim().toLowerCase())
            .map(user -> {
                if (user.getInterests() == null || user.getInterests().isBlank()) {
                    return List.of();
                }
                return List.of(user.getInterests().split(","));
            });
    }
}
