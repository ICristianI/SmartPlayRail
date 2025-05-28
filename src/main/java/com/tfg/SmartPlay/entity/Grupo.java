package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Entity que representa un grupo de usuarios en la aplicación SmartPlay, permitiendo la gestión de cuadernos y fichas compartidas entre los miembros del grupo.

@Entity
@Table(name = "grupos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del grupo no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Column
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;

    @Column(nullable = false, unique = true)
    private String codigoAcceso;

    @Column(nullable = false)
    private int intentosMaximosFicha = 1;

    @Column(nullable = false)
    private int intentosMaximosCuaderno = 1;


    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "creador_id", nullable = false)
    private User creador;

    @ManyToMany
    @JoinTable(name = "grupo_usuarios", joinColumns = @JoinColumn(name = "grupo_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    @Size(max = 100, message = "Un grupo no puede tener más de 100 usuarios")
    private List<User> usuarios;

    @ManyToMany
    @JoinTable(name = "grupo_cuadernos", joinColumns = @JoinColumn(name = "grupo_id"), inverseJoinColumns = @JoinColumn(name = "cuaderno_id"))
    private List<Cuaderno> cuadernos;

    // Método que se ejecuta antes de persistir el grupo en la base de datos, estableciendo la fecha de creación al momento actual.
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Método que devuelve la fecha de creación del grupo formateada como "dd/MM/yyyy".
    public String getFechaCreacionFormateada() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return fechaCreacion.format(formatter);
}

}
