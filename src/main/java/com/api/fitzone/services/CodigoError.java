package com.api.fitzone.services;

import org.springframework.http.HttpStatus;

public enum CodigoError {
    DATOS_INCOMPLETOS(HttpStatus.BAD_REQUEST, "Faltan datos obligatorios: idSocio e idClase"),
    SOCIO_NO_ENCONTRADO(HttpStatus.NOT_FOUND, "No existe el socio"),
    CLASE_NO_ENCONTRADA(HttpStatus.NOT_FOUND, "No existe la clase"),
    INSCRIPCION_NO_ENCONTRADA(HttpStatus.NOT_FOUND, "No existe la inscripción"),
    MEMBRESIA_VENCIDA(HttpStatus.CONFLICT, "La membresía del socio no está vigente para la fecha de la clase"),
    CLASE_YA_INICIADA(HttpStatus.CONFLICT, "La clase ya inició o ya pasó"),
    CLASE_SIN_CUPO(HttpStatus.CONFLICT, "La clase no tiene cupo disponible"),
    INSCRIPCION_DUPLICADA(HttpStatus.CONFLICT, "El socio ya está inscrito en esta clase");

    private final HttpStatus status;
    private final String mensaje;

    CodigoError(HttpStatus status, String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
    }

    public HttpStatus getStatus() { return status; }
    public String getMensaje() { return mensaje; }
}
