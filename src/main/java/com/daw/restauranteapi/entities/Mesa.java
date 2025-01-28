package com.daw.restauranteapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mesas")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El número de mesa no puede ser nulo")
    @Positive(message = "El número de mesa debe ser un valor positivo")
    private Long numeroMesa;
    @NotBlank
    private String descripcion;

    @OneToMany(targetEntity = Reserva.class, cascade = CascadeType.ALL,
            mappedBy = "mesa")
    @JsonIgnore
    private List<Reserva> reservas = new ArrayList<>();
}