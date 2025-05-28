package com.tfg.SmartPlay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.entity.VerificationToken;

// Repositorio para la entidad VerificationToken, que representa un token de verificación asociado a un usuario en la aplicación SmartPlay.
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser(User user);

    Optional<VerificationToken> findByUserId(Long userId);

}
