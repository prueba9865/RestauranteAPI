package com.daw.restauranteapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar en blanco")
    private String nombre;

    @Pattern(regexp = "^\\+34 \\d{9}$", message = "El formato del número de teléfono debe ser '+34 123456789'")
    private String telefono;

    @Column(unique = true)
    @Email(message = "Introduce un formato de email válido")
    @NotBlank(message = "El email no puede estar en blanco")
    private String email;

    @OneToMany(targetEntity = Reserva.class, cascade = CascadeType.ALL, mappedBy = "cliente")
    @JsonIgnore
    private List<Reserva> reservas = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}