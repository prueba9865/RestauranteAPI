package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.entities.Cliente;
import com.daw.restauranteapi.entities.Mesa;
import com.daw.restauranteapi.repositories.ClienteRepository;
import com.daw.restauranteapi.repositories.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MesaController {
    @Autowired
    private MesaRepository mesaRepository;

    /**
     * Obtener todas las mesas en un JSON
     */
    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> getListMesas(){
        List<Mesa> mesas = mesaRepository.findAll();
        return ResponseEntity.ok(mesas);    //Devuelve el código status 200 OK
    }

    /**
     * Obtiene una mesa
     */
    @GetMapping("/mesas/{id}")
    public ResponseEntity<Mesa> getMesa(@PathVariable Long id){
        /*
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if (empleado.isPresent()) {
            return ResponseEntity.ok().body(empleado.get()); // Devuelve el código status 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Devuelve el código 404 Not Found
        }
         */

        return mesaRepository.findById(id)
                .map(mesa -> ResponseEntity.ok().body(mesa))    //Devuelve el código status 200 OK
                .orElse(ResponseEntity.notFound().build());     //Devuelve el código 404 Not Found
    }

    /**
     * Insertar una mesa (recibe los datos en el cuerpo (body) en formato JSON)
     */
    @PostMapping("/mesas")
    public ResponseEntity<Mesa> insertMesa(@RequestBody Mesa mesa){
        Mesa mesaGuardada = mesaRepository.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaGuardada);    //Devuelve el código status 201 Created
    }

    /**
     * Modifica una mesa
     */
    @PutMapping("/mesas/{id}")
    public ResponseEntity<Mesa> editMesa(@PathVariable Long id, @RequestBody Mesa nuevaMesa){
        /*
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if(empleado.isPresent()){
            empleado.get().setNombre(nuevoEmpleado.getNombre());
            empleado.get().setEmail(nuevoEmpleado.getEmail());
            empleado.get().setApellidos(nuevoEmpleado.getApellidos());
            return empleadoRepository.save(empleado.get());
        }else{
            return empleadoRepository.save(nuevoEmpleado);
        }
        */

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

    /**
     * Borra una mesa
     */
    @DeleteMapping("/mesas/{id}")
    public ResponseEntity<?> deleteMesa(@PathVariable Long id){

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


        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesaRepository.delete(mesa);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
