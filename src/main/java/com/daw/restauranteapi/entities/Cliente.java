package com.daw.restauranteapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotBlank
    private String nombre;
    @NotBlank
    private String telefono;
    @Column(unique = true)
    @Email
    private String email;

    @OneToMany(targetEntity = Reserva.class, cascade = CascadeType.ALL,
            mappedBy = "cliente")
    @JsonIgnore
    private List<Reserva> reservas = new ArrayList<>();
}