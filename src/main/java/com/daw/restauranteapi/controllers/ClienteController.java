package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.entities.Cliente;
import com.daw.restauranteapi.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Obtener todos los clientes en un JSON
     */
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getListClientes(){
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);    //Devuelve el código status 200 OK
    }

    /**
     * Obtiene un cliente
     */
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> getCliente(@PathVariable Long id){
        /*
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if (empleado.isPresent()) {
            return ResponseEntity.ok().body(empleado.get()); // Devuelve el código status 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Devuelve el código 404 Not Found
        }
         */

        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok().body(cliente))    //Devuelve el código status 200 OK
                .orElse(ResponseEntity.notFound().build());     //Devuelve el código 404 Not Found
    }

    /**
     * Insertar un cliente (recibe los datos en el cuerpo (body) en formato JSON)
     */
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> insertCLiente(@RequestBody Cliente cliente){
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);    //Devuelve el código status 201 Created
    }

    /**
     * Modifica un cliente
     */
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> editCliente(@PathVariable Long id, @RequestBody Cliente nuevoCliente){
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

        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(nuevoCliente.getNombre());
                    cliente.setTelefono(nuevoCliente.getTelefono());
                    cliente.setEmail(nuevoCliente.getEmail());
                    return ResponseEntity.ok(clienteRepository.save(cliente));    //Devuelve el código 200 OK y en el cuerpo del mensaje el nuevo empleado en JSON
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();   //Devuelve el código 404 NotFound
                });
    }

    /**
     * Borra un cliente
     */
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id){

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


        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
