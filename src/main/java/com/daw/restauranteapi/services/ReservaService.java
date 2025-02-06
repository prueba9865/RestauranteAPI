package com.daw.restauranteapi.services;

import com.daw.restauranteapi.entities.Reserva;
import com.daw.restauranteapi.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;
    
    public ResponseEntity<?> obtenerReserva(Long id){
        if (id <= 0) {
            Map<String,String> res = new HashMap();
            res.put("error", "El numero no puede ser negativo");
            // Si el id no es válido, devolvemos un error 400
            return ResponseEntity.badRequest()
                    .body(res);  // Se puede enviar un mensaje adicional en el cuerpo si lo deseas
        }

        return reservaRepository.findById(id)
                .map(reserva -> ResponseEntity.ok().body(reserva))    //Devuelve el código status 200 OK
                .orElse(ResponseEntity.notFound().build());     //Devuelve el código 404 Not Found
    }

    public ResponseEntity<Reserva> insertarReserva(Reserva reserva){
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

    public ResponseEntity<?> borrarReserva(Long id){
        if (id <= 0) {
            Map<String,String> res = new HashMap();
            res.put("error", "El numero no puede ser negativo");
            // Si el id no es válido, devolvemos un error 400
            return ResponseEntity.badRequest()
                    .body(res);  // Se puede enviar un mensaje adicional en el cuerpo si lo deseas
        }


        return reservaRepository.findById(id)
                .map(reserva -> {
                    reservaRepository.delete(reserva);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
