package com.api.fitzone.services;

import com.api.fitzone.events.InscripcionRegistrada;
import com.api.fitzone.models.Clase;
import com.api.fitzone.models.Inscripcion;
import com.api.fitzone.models.Notificacion;
import com.api.fitzone.repositories.InscripcionRepository;
import com.api.fitzone.repositories.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final NotificacionRepository repositorio;
    private final InscripcionRepository inscripciones;
    private final int horasAntes;

    public NotificacionService(NotificacionRepository repositorio, InscripcionRepository inscripciones,
                               @Value("${fitzone.recordatorio.horas-antes:2}") int horasAntes) {
        this.repositorio = repositorio;
        this.inscripciones = inscripciones;
        this.horasAntes = horasAntes;
    }

    @Async
    @TransactionalEventListener
    public void alRegistrarseInscripcion(InscripcionRegistrada evento) {
        inscripciones.findById(evento.idInscripcion()).ifPresent(this::enviarConfirmacion);
    }

    public Notificacion enviarConfirmacion(Inscripcion inscripcion) {
        Clase clase = inscripcion.getClase();
        String mensaje = "Hola " + inscripcion.getSocio().getNombre() + ", tu lugar en " + clase.getDisciplina()
                + " (sucursal " + clase.getSucursal() + ") el " + clase.getFechaHora().format(FORMATO)
                + " está confirmado. Te recordaremos " + horasAntes + " horas antes.";
        return enviar(inscripcion, Notificacion.Tipo.CONFIRMACION, mensaje);
    }

    @Scheduled(fixedRateString = "${fitzone.recordatorio.intervalo-ms:60000}")
    @Transactional
    public void programarRecordatorio() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Inscripcion> pendientes = inscripciones.pendientesDeRecordatorio(ahora, ahora.plusHours(horasAntes));
        for (Inscripcion inscripcion : pendientes) {
            Clase clase = inscripcion.getClase();
            enviar(inscripcion, Notificacion.Tipo.RECORDATORIO, "Recordatorio: tu clase de " + clase.getDisciplina()
                    + " en sucursal " + clase.getSucursal() + " empieza a las " + clase.getFechaHora().format(FORMATO) + ".");
            inscripcion.setRecordatorioEnviado(true);
            inscripciones.save(inscripcion);
        }
    }

    public List<Notificacion> listar() {
        return repositorio.findAll();
    }

    public List<Notificacion> listarPorSocio(Long idSocio) {
        return repositorio.findByInscripcionSocioIdOrderByFechaEnvioDesc(idSocio);
    }

    private Notificacion enviar(Inscripcion inscripcion, Notificacion.Tipo tipo, String mensaje) {
        Notificacion notificacion = repositorio.save(new Notificacion(inscripcion, tipo, mensaje));
        log.info("[{}] Para: {} | {}", tipo, notificacion.getDestinatario(), mensaje);
        return notificacion;
    }
}
