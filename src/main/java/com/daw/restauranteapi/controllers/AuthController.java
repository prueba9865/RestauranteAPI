package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.DTO.LoginRequestDTO;
import com.daw.restauranteapi.DTO.LoginResponseDTO;
import com.daw.restauranteapi.DTO.RoleAssignmentDTO;
import com.daw.restauranteapi.DTO.UserRegisterDTO;
import com.daw.restauranteapi.config.JwtTokenProvider;
import com.daw.restauranteapi.entities.UserEntity;
import com.daw.restauranteapi.repositories.UserEntityRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String,String>> save(@RequestBody UserRegisterDTO userDTO) {
        try {
            if(userDTO.getPassword().equals(userDTO.getPassword2())){
                UserEntity userEntity = this.userRepository.save(
                        UserEntity.builder()
                                .username(userDTO.getUsername())
                                .password(passwordEncoder.encode(userDTO.getPassword()))
                                .email(userDTO.getEmail())
                                .authorities(List.of("ROLE_USER"))
                                .build());

                return ResponseEntity.status(HttpStatus.CREATED).body(
                        Map.of("message","Usuario creado exitosamente")
                );
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("error","Las credenciales no coinciden")
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Email o username ya utilizado"));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        try {

            //Validamos al usuario en Spring (hacemos login manualmente)
            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(userPassAuthToken);    //valida el usuario y devuelve un objeto Authentication con sus datos
            //Obtenemos el UserEntity del usuario logueado
            UserEntity user = (UserEntity) auth.getPrincipal();

            //Generamos un token con los datos del usuario (la clase tokenProvider ha hemos creado nosotros para no poner aquí todo el código
            String token = this.tokenProvider.generateToken(auth);

            //Devolvemos un código 200 con el username y token JWT
            return ResponseEntity.ok(new LoginResponseDTO(user.getUsername(), token));
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
    @PutMapping("/auth/asignarRol/{id}")
    public ResponseEntity<?> asignarRol(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        if (id <= 0) {
            // Si el id no es válido, devolvemos un error 400
            Map<String, String> res = new HashMap<>();
            res.put("error", "El número no puede ser negativo");
            return ResponseEntity.badRequest().body(res);
        }

        Optional<UserEntity> usuarioOpt = this.userRepository.findById(id);

        if (usuarioOpt.isEmpty()) {
            // Si el usuario no existe, devolvemos un error 404
            Map<String, String> res = new HashMap<>();
            res.put("error", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        UserEntity usuario = usuarioOpt.get();

        // Obtener las autoridades actuales y convertirlas a String
        List<String> authorities = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // Convertir cada GrantedAuthority a String
                .collect(Collectors.toList());  // Recogerlo en una lista de String

        // Verificar si el rol "ROLE_ADMIN" ya está asignado
        if (authorities.contains("ROLE_ADMIN")) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "El usuario ya tiene el rol ROLE_ADMIN");
            return ResponseEntity.badRequest().body(res);
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            Map<String, String> res = Map.of("error", "El token JWT es necesario en el encabezado Authorization");
            return ResponseEntity.status(401).body(res);
        }

        // Extraer el token JWT
        String token = authHeader.substring(7);  // Elimina el "Bearer " al principio del token

        Claims claims = null;
        // Decodificar el JWT y obtener las claims
        try {
            // ME LLEVA A ESTE CATCH, EL PROBLEMA SEGURAMENTE ESTE AQUI, O EN LA FUNCION getSecreykey(), o en el token
            claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            Map<String, String> res = Map.of("error", "El token JWT no es válido o ha expirado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        // Obtener los roles del JWT (supongamos que el campo "roles" contiene una lista de roles)
        List<String> roles = (List<String>) claims.get("roles");  // Suponiendo que tienes los roles en el claim "roles"

        // Verificar si el usuario tiene el rol ROLE_ADMIN
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            Map<String, String> res = Map.of("error", "No tienes permiso para asignar roles de administrador");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }

        // Asignar el nuevo rol "ROLE_ADMIN"
        authorities.add("ROLE_ADMIN");

        // Actualizar las autoridades en el usuario (como lista de String)
        usuario.setAuthorities(authorities);
        userRepository.save(usuario); // Guardar el usuario actualizado

        // Responder con un mensaje de éxito
        return ResponseEntity.ok(Map.of("message", "Rol de administrador asignado correctamente"));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode("zskfldj394852l3kj4tho9a8yt9qa4)()(%&asfdasdrtg45545·%·%");
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
