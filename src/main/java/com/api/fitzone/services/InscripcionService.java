package com.api.fitzone.services;

import com.api.fitzone.models.Clase;
import com.api.fitzone.models.Inscripcion;
import com.api.fitzone.models.Socio;
import com.api.fitzone.repositories.InscripcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscripcionService {

    private final InscripcionRepository repositorio;

    public InscripcionService(InscripcionRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Inscripcion registrarInscripcion(Socio socio, Clase clase) {
        return repositorio.save(new Inscripcion(socio, clase));
    }

    public long contarInscritos(Long idClase) {
        return repositorio.countByClaseId(idClase);
    }

    public boolean estaInscrito(Long idSocio, Long idClase) {
        return repositorio.existsBySocioIdAndClaseId(idSocio, idClase);
    }

    public Inscripcion consultarInscripcion(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ServicioException(CodigoError.INSCRIPCION_NO_ENCONTRADA, "id " + id));
    }

    public List<Inscripcion> listar() {
        return repositorio.findAll();
    }

    public List<Inscripcion> listarPorSocio(Long idSocio) {
        return repositorio.findBySocioIdOrderByClaseFechaHoraAsc(idSocio);
    }
}
