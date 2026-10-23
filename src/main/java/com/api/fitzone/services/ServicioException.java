package com.api.fitzone.services;

public class ServicioException extends RuntimeException {

    private final CodigoError codigo;

    public ServicioException(CodigoError codigo) {
        super(codigo.getMensaje());
        this.codigo = codigo;
    }

    public ServicioException(CodigoError codigo, String detalle) {
        super(codigo.getMensaje() + ": " + detalle);
        this.codigo = codigo;
    }

    public CodigoError getCodigo() { return codigo; }
}
