package com.api.fitzone;

import com.api.fitzone.models.Clase;
import com.api.fitzone.models.Inscripcion;
import com.api.fitzone.models.Socio;
import com.api.fitzone.repositories.ClaseRepository;
import com.api.fitzone.repositories.InscripcionRepository;
import com.api.fitzone.repositories.SocioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final SocioRepository socios;
    private final ClaseRepository clases;
    private final InscripcionRepository inscripciones;

    public DatosIniciales(SocioRepository socios, ClaseRepository clases, InscripcionRepository inscripciones) {
        this.socios = socios;
        this.clases = clases;
        this.inscripciones = inscripciones;
    }

    @Override
    public void run(String... args) {
        if (socios.count() > 0) {
            return;
        }
        LocalDate hoy = LocalDate.now();
        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime manana = hoy.plusDays(1).atStartOfDay();

        Socio ana = new Socio("Ana García", "ana.garcia@correo.com", "5511110001", Socio.TipoMembresia.MENSUAL, hoy.plusDays(10));
        Socio luis = new Socio("Luis Pérez", "luis.perez@correo.com", "5511110002", Socio.TipoMembresia.ANUAL, hoy.plusDays(200));
        Socio maria = new Socio("María Torres", "maria.torres@correo.com", "5511110003", Socio.TipoMembresia.MENSUAL, hoy.minusDays(5));
        Socio pedro = new Socio("Pedro Ramírez", "pedro.ramirez@correo.com", "5511110004", Socio.TipoMembresia.TRIMESTRAL, hoy.plusDays(60));
        Socio sofia = new Socio("Sofía Castro", "sofia.castro@correo.com", "5511110005", Socio.TipoMembresia.ANUAL, hoy.plusDays(90));
        Socio diego = new Socio("Diego Morales", "diego.morales@correo.com", "5511110006", Socio.TipoMembresia.MENSUAL, hoy.plusDays(30));
        socios.saveAll(List.of(ana, luis, maria, pedro, sofia, diego));

        Clase yoga = new Clase(Clase.Disciplina.YOGA, Clase.Sucursal.CENTRO, "Laura Méndez", manana.withHour(8), 60, 20);
        Clase spinning = new Clase(Clase.Disciplina.SPINNING, Clase.Sucursal.NORTE, "Carlos Ruiz", manana.withHour(18), 45, 3);
        Clase funcional = new Clase(Clase.Disciplina.FUNCIONAL, Clase.Sucursal.SUR, "Jorge Vega", ahora.plusMinutes(90), 50, 20);
        Clase yogaPasada = new Clase(Clase.Disciplina.YOGA, Clase.Sucursal.SUR, "Laura Méndez", ahora.minusHours(2), 60, 20);
        Clase funcionalMes = new Clase(Clase.Disciplina.FUNCIONAL, Clase.Sucursal.CENTRO, "Jorge Vega", hoy.plusDays(20).atTime(19, 0), 50, 20);
        clases.saveAll(List.of(yoga, spinning, funcional, yogaPasada, funcionalMes));

        inscripciones.saveAll(List.of(
                new Inscripcion(pedro, spinning),
                new Inscripcion(sofia, spinning),
                new Inscripcion(diego, spinning)));
    }
}
