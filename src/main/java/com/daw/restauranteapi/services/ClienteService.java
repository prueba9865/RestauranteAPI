package com.daw.restauranteapi.services;

import com.daw.restauranteapi.entities.Cliente;
import com.daw.restauranteapi.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    
    public ResponseEntity<?> obtenerCliente(Long id){
        if (id <= 0) {
            Map<String,String> res = new HashMap();
            res.put("error", "El numero no puede ser negativo");
            // Si el id no es válido, devolvemos un error 400
            return ResponseEntity.badRequest()
                    .body(res);  // Se puede enviar un mensaje adicional en el cuerpo si lo deseas
        }

        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok().body(cliente))    //Devuelve el código status 200 OK
                .orElse(ResponseEntity.notFound().build());     //Devuelve el código 404 Not Found
    }

    public ResponseEntity<?> editarCliente(Long id, Cliente nuevoCliente){
        if (id <= 0) {
            Map<String,String> res = new HashMap();
            res.put("error", "El numero no puede ser negativo");
            // Si el id no es válido, devolvemos un error 400
            return ResponseEntity.badRequest()
                    .body(res);  // Se puede enviar un mensaje adicional en el cuerpo si lo deseas
        }

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

    public ResponseEntity<?> borrarCliente(Long id){
        if (id <= 0) {
            Map<String,String> res = new HashMap();
            res.put("error", "El numero no puede ser negativo");
            // Si el id no es válido, devolvemos un error 400
            return ResponseEntity.badRequest()
                    .body(res);  // Se puede enviar un mensaje adicional en el cuerpo si lo deseas
        }

        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
