package com.api.fitzone.controllers;

import com.api.fitzone.models.Clase;
import com.api.fitzone.services.ClaseService;
import com.api.fitzone.services.ClaseService.CupoRespuesta;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clases")
public class ClaseController {

    private final ClaseService servicio;

    public ClaseController(ClaseService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Clase> listarProximas(@RequestParam(required = false) Clase.Sucursal sucursal) {
        return servicio.listarProximas(sucursal);
    }

    @GetMapping("/{id}")
    public Clase consultarClase(@PathVariable Long id) {
        return servicio.consultarClase(id);
    }

    @GetMapping("/{id}/cupo")
    public CupoRespuesta consultarCupo(@PathVariable Long id) {
        return servicio.consultarCupo(id);
    }
}
