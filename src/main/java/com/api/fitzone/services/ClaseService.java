package com.api.fitzone.services;

import com.api.fitzone.models.Clase;
import com.api.fitzone.repositories.ClaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaseService {

    public record CupoRespuesta(Long idClase, String disciplina, String sucursal, LocalDateTime fechaHora,
                                int capacidad, long inscritos, long disponibles, boolean hayCupo) {
    }

    private final ClaseRepository repositorio;
    private final InscripcionService inscripcionService;

    public ClaseService(ClaseRepository repositorio, InscripcionService inscripcionService) {
        this.repositorio = repositorio;
        this.inscripcionService = inscripcionService;
    }

    public List<Clase> listarProximas(Clase.Sucursal sucursal) {
        LocalDateTime ahora = LocalDateTime.now();
        return sucursal == null
                ? repositorio.findByFechaHoraAfterOrderByFechaHoraAsc(ahora)
                : repositorio.findBySucursalAndFechaHoraAfterOrderByFechaHoraAsc(sucursal, ahora);
    }

    public Clase consultarClase(Long id) {
        if (id == null) {
            throw new ServicioException(CodigoError.DATOS_INCOMPLETOS);
        }
        return repositorio.findById(id)
                .orElseThrow(() -> new ServicioException(CodigoError.CLASE_NO_ENCONTRADA, "id " + id));
    }

    public Clase bloquearParaInscribir(Long id) {
        if (id == null) {
            throw new ServicioException(CodigoError.DATOS_INCOMPLETOS);
        }
        return repositorio.bloquearParaInscribir(id)
                .orElseThrow(() -> new ServicioException(CodigoError.CLASE_NO_ENCONTRADA, "id " + id));
    }

    public CupoRespuesta consultarCupo(Long idClase) {
        return calcularCupo(consultarClase(idClase));
    }

    public CupoRespuesta calcularCupo(Clase clase) {
        long inscritos = inscripcionService.contarInscritos(clase.getId());
        long disponibles = Math.max(clase.getCapacidad() - inscritos, 0);
        return new CupoRespuesta(clase.getId(), clase.getDisciplina().name(), clase.getSucursal().name(),
                clase.getFechaHora(), clase.getCapacidad(), inscritos, disponibles, disponibles > 0);
    }
}
