package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity que representa la relación entre un usuario y un cuaderno en la aplicación SmartPlay para la gestión de notas.
@Entity
@Table(name = "cuaderno_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuadernoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "cuaderno_id", nullable = false)
    private Cuaderno cuaderno;

    private double nota;

}
