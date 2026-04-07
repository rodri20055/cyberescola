package pt.cyberescola.cyberescola.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}