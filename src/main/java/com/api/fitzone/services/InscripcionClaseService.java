package com.api.fitzone.services;

import com.api.fitzone.events.InscripcionRegistrada;
import com.api.fitzone.models.Clase;
import com.api.fitzone.models.Inscripcion;
import com.api.fitzone.models.Socio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscripcionClaseService {

    public record InscripcionRequest(Long idSocio, Long idClase) {
    }

    public record InscripcionRespuesta(Long idInscripcion, Long idSocio, String socio, Long idClase,
                                       String disciplina, String sucursal, String instructor,
                                       LocalDateTime fechaHoraClase, LocalDateTime fechaInscripcion,
                                       String estado, LocalDateTime recordatorioProgramado, boolean recordatorioEnviado) {
    }

    private final SocioService socioService;
    private final ClaseService claseService;
    private final InscripcionService inscripcionService;
    private final ApplicationEventPublisher eventos;
    private final int horasAntes;

    public InscripcionClaseService(SocioService socioService, ClaseService claseService,
                                   InscripcionService inscripcionService, ApplicationEventPublisher eventos,
                                   @Value("${fitzone.recordatorio.horas-antes:2}") int horasAntes) {
        this.socioService = socioService;
        this.claseService = claseService;
        this.inscripcionService = inscripcionService;
        this.eventos = eventos;
        this.horasAntes = horasAntes;
    }

    @Transactional
    public InscripcionRespuesta inscribirEnClase(InscripcionRequest request) {
        if (request == null || request.idSocio() == null || request.idClase() == null) {
            throw new ServicioException(CodigoError.DATOS_INCOMPLETOS);
        }
        Socio socio = socioService.consultarSocio(request.idSocio());
        Clase clase = claseService.bloquearParaInscribir(request.idClase());

        if (!clase.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new ServicioException(CodigoError.CLASE_YA_INICIADA, "fue el " + clase.getFechaHora());
        }
        socioService.verificarMembresiaVigente(socio, clase.getFechaHora().toLocalDate());
        if (inscripcionService.estaInscrito(socio.getId(), clase.getId())) {
            throw new ServicioException(CodigoError.INSCRIPCION_DUPLICADA);
        }
        if (!claseService.calcularCupo(clase).hayCupo()) {
            throw new ServicioException(CodigoError.CLASE_SIN_CUPO, "capacidad " + clase.getCapacidad());
        }

        Inscripcion inscripcion = inscripcionService.registrarInscripcion(socio, clase);
        eventos.publishEvent(new InscripcionRegistrada(inscripcion.getId()));
        return aRespuesta(inscripcion);
    }

    public InscripcionRespuesta consultar(Long id) {
        return aRespuesta(inscripcionService.consultarInscripcion(id));
    }

    public List<InscripcionRespuesta> listar() {
        return inscripcionService.listar().stream().map(this::aRespuesta).toList();
    }

    public List<InscripcionRespuesta> listarPorSocio(Long idSocio) {
        socioService.consultarSocio(idSocio);
        return inscripcionService.listarPorSocio(idSocio).stream().map(this::aRespuesta).toList();
    }

    private InscripcionRespuesta aRespuesta(Inscripcion i) {
        Clase c = i.getClase();
        return new InscripcionRespuesta(i.getId(), i.getSocio().getId(), i.getSocio().getNombre(), c.getId(),
                c.getDisciplina().name(), c.getSucursal().name(), c.getInstructor(), c.getFechaHora(),
                i.getFechaInscripcion(), "CONFIRMADA", c.getFechaHora().minusHours(horasAntes), i.isRecordatorioEnviado());
    }
}
