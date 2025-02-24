package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.DTO.LoginRequestDTO;
import com.daw.restauranteapi.DTO.LoginResponseDTO;
import com.daw.restauranteapi.config.JwtTokenProvider;
import com.daw.restauranteapi.entities.Cliente;
import com.daw.restauranteapi.entities.UserEntity;
import com.daw.restauranteapi.repositories.ClienteRepository;
import com.daw.restauranteapi.repositories.UserEntityRepository;
import com.daw.restauranteapi.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
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
    @PostMapping("/clientes/register")
    public ResponseEntity<Cliente> registerCliente(@RequestBody @Valid Cliente cliente) {
        return clienteService.registrarCliente(cliente);
    }

    @PostMapping("/clientes/login")
    public ResponseEntity<?> loginCliente(@RequestBody @Valid LoginRequestDTO loginDTO) {
        return clienteService.logearCliente(loginDTO);
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
