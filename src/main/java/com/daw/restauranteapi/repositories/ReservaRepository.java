package com.daw.restauranteapi.repositories;

import com.daw.restauranteapi.DTO.ReservaDTO;
import com.daw.restauranteapi.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query(value = "SELECT * FROM reservas r WHERE r.fecha >= :fecha AND " +
            "(r.hora_inicio BETWEEN :horaInicio AND :horaFin OR " +
            "r.hora_fin BETWEEN :horaInicio AND :horaFin)", nativeQuery = true)
    List<Reserva> findConflictingReservations(LocalDate fecha, String horaInicio, String horaFin);

    // Si usamos esta query nativa necesitamos en la clase DTO pasar la fecha a LocalDate, ya que desde la DB
    // llega como Date
    /*@Query(value = "SELECT r.fecha, r.hora_inicio, r.hora_fin, r.numero_comensales, c.nombre, c.telefono, c.email, m.numero_mesa, m.descripcion " +
            "FROM reservas r " +
            "INNER JOIN clientes c ON r.id_cliente = c.id " +
            "INNER JOIN mesas m ON r.id_mesa = m.id " +
            "WHERE r.fecha = :fecha", nativeQuery = true)
    List<ReservaDTO> findReservasByFecha(@Param("fecha") LocalDate fecha);*/

    // Usando JPQL te castea la fecha como LocalDate automaticamente
    @Query("SELECT new com.daw.restauranteapi.DTO.ReservaDTO(r.fecha, r.horaInicio, r.horaFin, r.numeroComensales, " +
            "c.nombre, c.telefono, c.email, m.numeroMesa, m.descripcion) " +
            "FROM Reserva r " +
            "JOIN r.cliente c " +
            "JOIN r.mesa m " +
            "WHERE r.fecha = :fecha")
    List<ReservaDTO> findReservasByFecha(@Param("fecha") LocalDate fecha);

    List<Reserva> findByClienteId(Long clienteId); // Buscar reservas por ID de cliente

}