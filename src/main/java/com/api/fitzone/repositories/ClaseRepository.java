package com.api.fitzone.repositories;

import com.api.fitzone.models.Clase;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClaseRepository extends JpaRepository<Clase, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Clase c WHERE c.id = :id")
    Optional<Clase> bloquearParaInscribir(@Param("id") Long id);

    List<Clase> findByFechaHoraAfterOrderByFechaHoraAsc(LocalDateTime desde);

    List<Clase> findBySucursalAndFechaHoraAfterOrderByFechaHoraAsc(Clase.Sucursal sucursal, LocalDateTime desde);
}
