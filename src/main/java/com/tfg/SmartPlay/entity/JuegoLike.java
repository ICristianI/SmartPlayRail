package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import lombok.*;

// Entity que representa un "like" de un usuario a un juego en la aplicación SmartPlay, permitiendo a los usuarios expresar su aprobación o interés por juegos específicos.
@Entity
@Table(name = "juego_likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"juego_id", "usuario_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuegoLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;
}
