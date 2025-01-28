package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.DTO.ReservaDTO;
import com.daw.restauranteapi.entities.Mesa;
import com.daw.restauranteapi.entities.Reserva;
import com.daw.restauranteapi.repositories.MesaRepository;
import com.daw.restauranteapi.repositories.ReservaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservaController {
    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Obtener todas las reservas en un JSON
     */
    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> getListReserva(){
        List<Reserva> reservas = reservaRepository.findAll();
        return ResponseEntity.ok(reservas);    //Devuelve el código status 200 OK
    }

    /**
     * Obtiene una reserva
     */
    @GetMapping("/reservas/{id}")
    public ResponseEntity<Reserva> getReserva(@PathVariable Long id){
        /*
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if (empleado.isPresent()) {
            return ResponseEntity.ok().body(empleado.get()); // Devuelve el código status 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Devuelve el código 404 Not Found
        }
         */

        return reservaRepository.findById(id)
                .map(reserva -> ResponseEntity.ok().body(reserva))    //Devuelve el código status 200 OK
                .orElse(ResponseEntity.notFound().build());     //Devuelve el código 404 Not Found
    }

    /**
     * Insertar una reserva (recibe los datos en el cuerpo (body) en formato JSON)
     */
    @PostMapping("/reservas")
    public ResponseEntity<Reserva> insertReserva(@RequestBody Reserva reserva){
        // Validación simple de que horaInicio no sea mayor que horaFin
        if (reserva.getHoraInicio().compareTo(reserva.getHoraFin()) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid request
        }

        List<Reserva> reservasExistentes = reservaRepository.findConflictingReservations(
                reserva.getFecha(),
                reserva.getHoraInicio(),
                reserva.getHoraFin());

        if (!reservasExistentes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }




    /**
     * Borra una reserva
     */
    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id){

        /*
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if(empleado.isPresent()){
            empleadoRepository.delete(empleado.get());
            return ResponseEntity.noContent().build();  //Devuelve el código status 204 Not Content
        }
        else{
            return ResponseEntity.notFound().build();   //Devuelve el código status 404 Not Found
        }
        */


        return reservaRepository.findById(id)
                .map(reserva -> {
                    reservaRepository.delete(reserva);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las reservas en base a una fecha exacta (formato: ddmmyyyy)
     */
    @GetMapping("/reservas/fecha/{fecha}")
    public ResponseEntity<List<ReservaDTO>> getReservasByFecha(@PathVariable
                                                                   @DateTimeFormat(pattern = "ddMMyyyy")
                                                                   LocalDate fecha) {
        List<ReservaDTO> reservas = reservaRepository.findReservasByFecha(fecha);
        return ResponseEntity.ok(reservas);
    }


}
