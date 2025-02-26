package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.entities.Mesa;
import com.daw.restauranteapi.repositories.MesaRepository;
import com.daw.restauranteapi.repositories.ReservaRepository;
import com.daw.restauranteapi.services.MesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
public class MesaController {
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private MesaService mesaService;

    /**
     * Obtener todas las mesas en un JSON
     */
    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> getListMesas(){
        List<Mesa> mesas = mesaRepository.findAll();
        return ResponseEntity.ok(mesas);    //Devuelve el código status 200 OK
    }

    @GetMapping("/mesasDisponibles")
    public ResponseEntity<List<Mesa>> obtenerMesasDisponibles(
            @RequestParam String fecha,
            @RequestParam String horaInicio,
            @RequestParam String horaFin) {

        LocalDate fechaReserva = LocalDate.parse(fecha, DateTimeFormatter.ISO_LOCAL_DATE);

        List<Mesa> mesasDisponibles = mesaService.obtenerMesasDisponibles(fechaReserva, horaInicio, horaFin);

        if (mesasDisponibles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } else {
            return ResponseEntity.ok(mesasDisponibles);
        }
    }

    /**
     * Obtiene una mesa
     */
    @GetMapping("/mesas/{id}")
    public ResponseEntity<?> getMesa(@PathVariable Long id){
        return mesaService.obtenerMesa(id);
    }

    /**
     * Insertar una mesa (recibe los datos en el cuerpo (body) en formato JSON)
     */
    @PostMapping("/mesas")
    public ResponseEntity<Mesa> insertMesa(@RequestBody @Valid Mesa mesa){
        Mesa mesaGuardada = mesaRepository.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaGuardada);    //Devuelve el código status 201 Created
    }

    /**
     * Modifica una mesa
     */
    @PutMapping("/mesas/{id}")
    public ResponseEntity<?> editMesa(@PathVariable Long id, @RequestBody @Valid Mesa nuevaMesa){
        return mesaService.editarMesa(id, nuevaMesa);
    }

    /**
     * Borra una mesa
     */
    @DeleteMapping("/mesas/{id}")
    public ResponseEntity<?> deleteMesa(@PathVariable Long id){
        return mesaService.borrarMesa(id);
    }
}
