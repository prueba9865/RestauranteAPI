package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.DTO.ReservaDTO;
import com.daw.restauranteapi.entities.Reserva;
import com.daw.restauranteapi.repositories.ReservaRepository;
import com.daw.restauranteapi.services.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservaController {
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ReservaService reservaService;

    /**
     * Obtener todas las reservas en un JSON
     */
    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> getListReserva(@RequestParam Long clienteId) {
        List<Reserva> reservas = reservaRepository.findByClienteId(clienteId); // Filtrar por ID de cliente
        return ResponseEntity.ok(reservas); // Devuelve el código status 200 OK
    }

    /**
     * Obtiene una reserva
     */
    @GetMapping("/reservas/{id}")
    public ResponseEntity<?> getReserva(@PathVariable Long id){
        return reservaService.obtenerReserva(id);
    }

    /**
     * Insertar una reserva (recibe los datos en el cuerpo (body) en formato JSON)
     */
    @PostMapping("/reservas")
    public ResponseEntity<Reserva> insertReserva(@RequestBody @Valid Reserva reserva){
        return reservaService.insertarReserva(reserva);
    }


    /**
     * Borra una reserva
     */
    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id){
        return reservaService.borrarReserva(id);
    }

    /**
     * Obtiene todas las reservas en base a una fecha exacta (formato: ddmmyyyy) '/reservas/fecha/22122023'
     */
    @GetMapping("/reservas/fecha/{fecha}")
    public ResponseEntity<?> getReservasByFecha(@PathVariable @DateTimeFormat(pattern = "ddMMyyyy")
                                                                   LocalDate fecha) {
        List<ReservaDTO> reservas = reservaRepository.findReservasByFecha(fecha);
        return ResponseEntity.ok(reservas);
    }


}
