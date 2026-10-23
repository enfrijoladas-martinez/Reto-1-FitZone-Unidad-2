package com.api.fitzone.controllers;

import com.api.fitzone.models.Socio;
import com.api.fitzone.services.SocioService;
import com.api.fitzone.services.SocioService.MembresiaRespuesta;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/socios")
public class SocioController {

    private final SocioService servicio;

    public SocioController(SocioService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Socio> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Socio consultarSocio(@PathVariable Long id) {
        return servicio.consultarSocio(id);
    }

    @GetMapping("/{id}/membresia")
    public MembresiaRespuesta consultarMembresia(@PathVariable Long id) {
        return servicio.consultarMembresia(id);
    }
}
