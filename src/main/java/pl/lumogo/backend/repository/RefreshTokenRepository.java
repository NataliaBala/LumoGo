package pl.lumogo.backend.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.lumogo.backend.model.RefreshToken;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select token from RefreshToken token where token.tokenHash = :tokenHash and token.expiresAt > :now")
    Optional<RefreshToken> findValidForUpdate(
        @Param("tokenHash") String tokenHash,
        @Param("now") Instant now
    );

    void deleteByTokenHash(String tokenHash);
}
