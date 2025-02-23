package com.daw.restauranteapi.repositories;

import com.daw.restauranteapi.entities.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    @Query("SELECT m FROM Mesa m WHERE m.id NOT IN (" +
            "SELECT r.mesa.id FROM Reserva r WHERE r.fecha = :fecha " +
            "AND ((r.horaInicio <= :hora AND r.horaFin > :hora) OR " +
            "(r.horaInicio < :hora AND r.horaFin >= :hora)))")
    List<Mesa> findMesasDisponibles(@Param("fecha") LocalDate fecha, @Param("hora") LocalTime hora);
}
