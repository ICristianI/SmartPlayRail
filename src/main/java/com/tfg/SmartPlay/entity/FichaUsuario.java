package com.tfg.SmartPlay.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ficha_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "ficha_id", nullable = false)
    private Ficha ficha;

    private double nota;
    
}
