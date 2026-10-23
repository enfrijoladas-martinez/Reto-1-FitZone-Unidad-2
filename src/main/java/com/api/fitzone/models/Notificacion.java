package com.api.fitzone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    public enum Tipo { CONFIRMACION, RECORDATORIO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_inscripcion", nullable = false)
    private Inscripcion inscripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Tipo tipo;

    @Column(nullable = false, length = 100)
    private String destinatario;

    @Column(nullable = false, length = 300)
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    public Notificacion() {
    }

    public Notificacion(Inscripcion inscripcion, Tipo tipo, String mensaje) {
        this.inscripcion = inscripcion;
        this.tipo = tipo;
        this.destinatario = inscripcion.getSocio().getCorreo();
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getIdInscripcion() { return inscripcion.getId(); }
    public Tipo getTipo() { return tipo; }
    public String getDestinatario() { return destinatario; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
}
