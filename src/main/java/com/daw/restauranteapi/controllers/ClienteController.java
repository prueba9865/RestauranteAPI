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
        // Verificar si el UserEntity existe y asignar el rol ROLE_USER
        if (cliente.getUser() != null && cliente.getUser().getId() == null) {
            UserEntity user = cliente.getUser();
            String nombre = cliente.getUser().getUsername();
            System.out.println(nombre);
            user.setUsername(user.getUsername());
            user.setAuthorities(List.of("ROLE_USER")); // Asignar el rol automáticamente
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UserEntity savedUser = userRepository.save(user); // Guardar el UserEntity
            cliente.setUser(savedUser);
        }

        // Guardar el Cliente
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @PostMapping("/clientes/login")
    public ResponseEntity<?> loginCliente(@RequestBody @Valid LoginRequestDTO loginDTO) {
        try {

            //Validamos al usuario en Spring (hacemos login manualmente)
            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(userPassAuthToken);    //valida el usuario y devuelve un objeto Authentication con sus datos
            //Obtenemos el UserEntity del usuario logueado
            UserEntity user = (UserEntity) auth.getPrincipal();

            //Generamos un token con los datos del usuario (la clase tokenProvider ha hemos creado nosotros para no poner aquí todo el código
            String token = this.tokenProvider.generateToken(auth);

            //Devolvemos un código 200 con el username y token JWT
            return ResponseEntity.ok(new LoginResponseDTO(user.getId(), user.getUsername(), token));
        }catch (Exception e) {  //Si el usuario no es válido, salta una excepción BadCredentialsException
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "path", "/auth/login",
                            "message", "Credenciales erróneas",
                            "timestamp", new Date()
                    )
            );
        }
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
