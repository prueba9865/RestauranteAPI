package com.daw.restauranteapi.services;

import com.daw.restauranteapi.entities.Mesa;
import com.daw.restauranteapi.entities.Reserva;
import com.daw.restauranteapi.repositories.MesaRepository;
import com.daw.restauranteapi.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MesaService {
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    public List<Mesa> obtenerMesasDisponibles(LocalDate fecha, String horaInicio, String horaFin) {
        // Obtener todas las mesas
        List<Mesa> todasLasMesas = mesaRepository.findAll();

        // Obtener las reservas conflictivas
        List<Reserva> reservasConflictivas = reservaRepository.findConflictingReservations(fecha, horaInicio, horaFin);

        // Obtener las mesas reservadas
        Set<Long> mesasReservadas = reservasConflictivas.stream()
                .map(reserva -> reserva.getMesa().getId())
                .collect(Collectors.toSet());

        // Filtrar las mesas disponibles
        return todasLasMesas.stream()
                .filter(mesa -> !mesasReservadas.contains(mesa.getId()))
                .collect(Collectors.toList());
    }
    
    public ResponseEntity<?> obtenerMesa(Long id){
        if (id <= 0) {
            Map<String,String> res = new HashMap();
            res.put("error", "El numero no puede ser negativo");
            // Si el id no es válido, devolvemos un error 400
            return ResponseEntity.badRequest()
                    .body(res);  // Se puede enviar un mensaje adicional en el cuerpo si lo deseas
        }

        return mesaRepository.findById(id)
                .map(mesa -> ResponseEntity.ok().body(mesa))    //Devuelve el código status 200 OK
                .orElse(ResponseEntity.notFound().build());     //Devuelve el código 404 Not Found
    }

    public ResponseEntity<?> editarMesa(Long id, Mesa nuevaMesa){
        if (id <= 0) {
            Map<String,String> res = new HashMap();
            res.put("error", "El numero no puede ser negativo");
            // Si el id no es válido, devolvemos un error 400
            return ResponseEntity.badRequest()
                    .body(res);  // Se puede enviar un mensaje adicional en el cuerpo si lo deseas
        }

        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesa.setNumeroMesa(nuevaMesa.getNumeroMesa());
                    mesa.setDescripcion(nuevaMesa.getDescripcion());
                    return ResponseEntity.ok(mesaRepository.save(mesa));    //Devuelve el código 200 OK y en el cuerpo del mensaje el nuevo empleado en JSON
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();   //Devuelve el código 404 NotFound
                });
    }

    public ResponseEntity<?> borrarMesa(Long id){
        if (id <= 0) {
            Map<String,String> res = new HashMap();
            res.put("error", "El numero no puede ser negativo");
            // Si el id no es válido, devolvemos un error 400
            return ResponseEntity.badRequest()
                    .body(res);  // Se puede enviar un mensaje adicional en el cuerpo si lo deseas
        }

        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesaRepository.delete(mesa);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
