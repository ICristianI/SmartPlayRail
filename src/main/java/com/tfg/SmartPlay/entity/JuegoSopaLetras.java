package com.tfg.SmartPlay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Entity que representa un juego de sopa de letras en la aplicación SmartPlay, permitiendo a los usuarios jugar encontrando palabras ocultas en una cuadrícula de letras.
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JuegoSopaLetras extends Juego {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String palabras;

}
