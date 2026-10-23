package com.api.fitzone.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "clase")
public class Clase {

    public static final int CAPACIDAD_MAXIMA = 20;

    public enum Disciplina { YOGA, SPINNING, FUNCIONAL }

    public enum Sucursal { CENTRO, NORTE, SUR }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Disciplina disciplina;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sucursal sucursal;

    @Column(nullable = false, length = 100)
    private String instructor;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private int duracionMinutos;

    @Column(nullable = false)
    private int capacidad;

    public Clase() {
    }

    public Clase(Disciplina disciplina, Sucursal sucursal, String instructor, LocalDateTime fechaHora,
                 int duracionMinutos, int capacidad) {
        this.disciplina = disciplina;
        this.sucursal = sucursal;
        this.instructor = instructor;
        this.fechaHora = fechaHora;
        this.duracionMinutos = duracionMinutos;
        this.capacidad = Math.min(capacidad, CAPACIDAD_MAXIMA);
    }

    public Long getId() { return id; }
    public Disciplina getDisciplina() { return disciplina; }
    public Sucursal getSucursal() { return sucursal; }
    public String getInstructor() { return instructor; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public int getCapacidad() { return capacidad; }
}
