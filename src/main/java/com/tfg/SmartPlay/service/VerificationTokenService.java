package com.tfg.SmartPlay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.entity.VerificationToken;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

// Servicio para gestionar los tokens de verificación de usuarios.

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    // Envía un correo de verificación al usuario.
    // Si ya existe un token para el usuario, lo elimina y crea uno nuevo.

    public void sendVerificationEmail(User user) {
        Optional<VerificationToken> existingToken = tokenRepository.findByUser(user);

        if (existingToken.isPresent()) {

            tokenRepository.delete(existingToken.get());
            tokenRepository.flush();

        }

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        tokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:8080/users/verificar?token=" + token;
        String message = "Haz clic en el siguiente enlace para verificar tu cuenta: " + verificationLink;

        sendEmail(user.getEmail(), "Verifica tu cuenta", message);
    }

    // Verifica un token de verificación.
    // Si el token es válido, activa la cuenta del usuario y elimina el token.

    public void verifyToken(String token) {
        Optional<VerificationToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            VerificationToken verificationToken = optionalToken.get();
            if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Token expirado");
            }

            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);

            tokenRepository.delete(verificationToken);
        } else {
            throw new RuntimeException("Token inválido");
        }
    }

    // Envía un correo al usuario y copia al administrador
    
    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        // Destinatarios: usuario + administrador
        mailMessage.setTo(new String[] { to, "smartplaysoporte@gmail.com" });

        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        mailSender.send(mailMessage);
    }

}
