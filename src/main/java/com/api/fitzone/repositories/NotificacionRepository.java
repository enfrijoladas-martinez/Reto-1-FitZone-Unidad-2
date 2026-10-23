package com.api.fitzone.repositories;

import com.api.fitzone.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByInscripcionSocioIdOrderByFechaEnvioDesc(Long idSocio);
}
