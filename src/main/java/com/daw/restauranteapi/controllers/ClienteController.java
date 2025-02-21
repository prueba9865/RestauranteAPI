package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.entities.Cliente;
import com.daw.restauranteapi.entities.UserEntity;
import com.daw.restauranteapi.repositories.ClienteRepository;
import com.daw.restauranteapi.repositories.UserEntityRepository;
import com.daw.restauranteapi.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private UserEntityRepository userRepository;
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
    public ResponseEntity<?> getCliente(@PathVariable Long id){
        return clienteService.obtenerCliente(id);
    }

    /**
     * Insertar un cliente (recibe los datos en el cuerpo (body) en formato JSON)
     */
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> insertCliente(@RequestBody @Valid Cliente cliente) {
        // Verificar si el UserEntity existe y asignar el rol ROLE_USER
        if (cliente.getUser() != null && cliente.getUser().getId() == null) {
            UserEntity user = cliente.getUser();
            user.setAuthorities(List.of("ROLE_USER")); // Asignar el rol automáticamente
            UserEntity savedUser = userRepository.save(user); // Guardar el UserEntity
            cliente.setUser(savedUser);
        }

        // Guardar el Cliente
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }
    /**
     * Modifica un cliente
     */
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> editCliente(@PathVariable Long id, @RequestBody @Valid Cliente nuevoCliente){
        return clienteService.editarCliente(id, nuevoCliente);
    }

    /**
     * Borra un cliente
     */
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id){
        return clienteService.borrarCliente(id);
    }
}
