package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import lombok.*;

// Entity que representa un "like" de un usuario a una ficha en la aplicación SmartPlay, permitiendo a los usuarios expresar su aprobación o interés por fichas específicas.

@Entity
@Table(name = "fichas_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "ficha_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "ficha_id", nullable = false)
    private Ficha ficha;
}
