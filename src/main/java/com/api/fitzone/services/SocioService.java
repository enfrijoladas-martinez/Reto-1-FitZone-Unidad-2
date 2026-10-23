package com.api.fitzone.services;

import com.api.fitzone.models.Socio;
import com.api.fitzone.repositories.SocioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SocioService {

    public record MembresiaRespuesta(Long idSocio, String socio, String tipoMembresia, LocalDate vencimiento,
                                     boolean vigente, long diasRestantes) {
    }

    private final SocioRepository repositorio;

    public SocioService(SocioRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Socio> listar() {
        return repositorio.findAll();
    }

    public Socio consultarSocio(Long id) {
        if (id == null) {
            throw new ServicioException(CodigoError.DATOS_INCOMPLETOS);
        }
        return repositorio.findById(id)
                .orElseThrow(() -> new ServicioException(CodigoError.SOCIO_NO_ENCONTRADO, "id " + id));
    }

    public MembresiaRespuesta consultarMembresia(Long idSocio) {
        Socio socio = consultarSocio(idSocio);
        LocalDate hoy = LocalDate.now();
        long dias = ChronoUnit.DAYS.between(hoy, socio.getVencimientoMembresia());
        return new MembresiaRespuesta(socio.getId(), socio.getNombre(), socio.getTipoMembresia().name(),
                socio.getVencimientoMembresia(), dias >= 0, Math.max(dias, 0));
    }

    public void verificarMembresiaVigente(Socio socio, LocalDate fechaClase) {
        if (socio.getVencimientoMembresia().isBefore(fechaClase)) {
            throw new ServicioException(CodigoError.MEMBRESIA_VENCIDA,
                    "vence el " + socio.getVencimientoMembresia() + " y la clase es el " + fechaClase);
        }
    }
}
