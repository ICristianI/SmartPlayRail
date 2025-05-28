package com.tfg.SmartPlay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Entity que representa un juego de ahorcado en la aplicación SmartPlay, permitiendo a los usuarios jugar adivinando palabras con un número limitado de intentos.
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JuegoAhorcado extends Juego {

    @Column(nullable = false)
    private String palabra;

    @Column(nullable = false)
    private int maxIntentos;
}
