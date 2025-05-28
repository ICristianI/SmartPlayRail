package com.tfg.SmartPlay.entity;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity que representa una ficha interactiva en la aplicación SmartPlay, que puede contener información educativa y elementos multimedia.

@Entity
@Table(name = "fichas_interactivas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ficha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "El idioma no puede estar vacío")
    @Size(max = 50, message = "El idioma no puede exceder los 50 caracteres")
    private String idioma;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "La asignatura no puede estar vacía")
    @Size(max = 50, message = "La asignatura no puede exceder los 50 caracteres")
    private String asignatura;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 1000, message = "El contenido no puede exceder los 1000 caracteres")
    private String contenido;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private Blob imagen;

    @Column(nullable = false)
    private boolean privada = false;

    @Column(columnDefinition = "TEXT")
    private String elementosSuperpuestos;

    @Column(nullable = false)
    private int meGusta = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToMany(mappedBy = "fichas")
    private List<Cuaderno> cuadernos;

    // Método que se ejecuta antes de persistir la ficha en la base de datos, estableciendo la fecha de creación al momento actual.
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Método que devuelve la fecha de creación de la ficha en un formato legible (dd/MM/yyyy).
    public String getFechaCreacionFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fechaCreacion.format(formatter);
    }

}
