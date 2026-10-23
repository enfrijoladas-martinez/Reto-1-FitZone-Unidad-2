package com.api.fitzone.repositories;

import com.api.fitzone.models.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    long countByClaseId(Long idClase);

    boolean existsBySocioIdAndClaseId(Long idSocio, Long idClase);

    List<Inscripcion> findBySocioIdOrderByClaseFechaHoraAsc(Long idSocio);

    @Query("SELECT i FROM Inscripcion i WHERE i.recordatorioEnviado = false AND i.clase.fechaHora > :ahora AND i.clase.fechaHora <= :limite")
    List<Inscripcion> pendientesDeRecordatorio(@Param("ahora") LocalDateTime ahora, @Param("limite") LocalDateTime limite);
}
