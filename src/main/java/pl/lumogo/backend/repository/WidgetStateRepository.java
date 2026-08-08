package pl.lumogo.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lumogo.backend.model.WidgetState;

import java.util.Optional;

@Transactional
public interface WidgetStateRepository extends JpaRepository<WidgetState, Long> {
    Optional<WidgetState> findByEmail(String email);
}
