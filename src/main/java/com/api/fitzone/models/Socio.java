package com.api.fitzone.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "socio")
public class Socio {

    public enum TipoMembresia { MENSUAL, TRIMESTRAL, ANUAL }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMembresia tipoMembresia;

    @Column(nullable = false)
    private LocalDate vencimientoMembresia;

    public Socio() {
    }

    public Socio(String nombre, String correo, String telefono, TipoMembresia tipoMembresia, LocalDate vencimientoMembresia) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.tipoMembresia = tipoMembresia;
        this.vencimientoMembresia = vencimientoMembresia;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public TipoMembresia getTipoMembresia() { return tipoMembresia; }
    public LocalDate getVencimientoMembresia() { return vencimientoMembresia; }
}
