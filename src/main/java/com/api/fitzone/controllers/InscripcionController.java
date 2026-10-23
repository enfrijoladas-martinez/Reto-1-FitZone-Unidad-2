package com.api.fitzone.controllers;

import com.api.fitzone.services.InscripcionClaseService;
import com.api.fitzone.services.InscripcionClaseService.InscripcionRequest;
import com.api.fitzone.services.InscripcionClaseService.InscripcionRespuesta;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
public class InscripcionController {

    private final InscripcionClaseService servicio;

    public InscripcionController(InscripcionClaseService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InscripcionRespuesta inscribirEnClase(@RequestBody InscripcionRequest request) {
        return servicio.inscribirEnClase(request);
    }

    @GetMapping
    public List<InscripcionRespuesta> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public InscripcionRespuesta consultar(@PathVariable Long id) {
        return servicio.consultar(id);
    }

    @GetMapping("/socio/{idSocio}")
    public List<InscripcionRespuesta> listarPorSocio(@PathVariable Long idSocio) {
        return servicio.listarPorSocio(idSocio);
    }
}
