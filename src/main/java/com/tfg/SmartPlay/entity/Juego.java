package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Entity que representa un juego en la aplicación SmartPlay, que puede ser de tipo "Juego de mesa" o "Juego de rol", y contiene información educativa y elementos multimedia.
@Entity
@Table(name = "juegos")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El idioma no puede estar vacío")
    @Size(max = 50, message = "El idioma no puede exceder los 50 caracteres")
    private String idioma;

    @Column(nullable = false)
    @NotBlank(message = "La asignatura no puede estar vacía")
    @Size(max = 50, message = "La asignatura no puede exceder los 50 caracteres")
    private String asignatura;

    @Size(max = 500, message = "El contenido no puede exceder los 1000 caracteres")
    private String contenido;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Column(nullable = false)
    private boolean privada = false;

    @Column(nullable = false)
    private int meGusta = 0;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime fechaCreacion;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private Blob imagen;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToMany
    @JoinTable(name = "cuaderno_juegos", joinColumns = @JoinColumn(name = "juego_id"), inverseJoinColumns = @JoinColumn(name = "cuaderno_id"))
    private List<Cuaderno> cuadernos;

    // Método que se ejecuta antes de persistir el juego en la base de datos adaptando la fecha de creación al momento actual.
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = java.time.LocalDateTime.now();
    }

    // Método que devuelve la fecha de creación del juego formateada como "dd/MM/yyyy".
    public String getFechaCreacionFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fechaCreacion.format(formatter);
    }
}
