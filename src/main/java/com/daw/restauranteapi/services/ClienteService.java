package com.daw.restauranteapi.services;

import com.daw.restauranteapi.DTO.LoginRequestDTO;
import com.daw.restauranteapi.DTO.LoginResponseDTO;
import com.daw.restauranteapi.config.JwtTokenProvider;
import com.daw.restauranteapi.entities.Cliente;
import com.daw.restauranteapi.entities.UserEntity;
import com.daw.restauranteapi.repositories.ClienteRepository;
import com.daw.restauranteapi.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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

    public ResponseEntity<?> logearCliente(LoginRequestDTO loginDTO){
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

    public ResponseEntity<Cliente> registrarCliente(Cliente cliente){
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
}
