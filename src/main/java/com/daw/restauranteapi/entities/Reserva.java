package com.daw.restauranteapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    @NotBlank
    private String horaInicio;
    @NotBlank
    private String horaFin;
    private Integer numeroComensales;

    @ManyToOne(targetEntity = Cliente.class)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(targetEntity = Mesa.class)
    @JoinColumn(name = "id_mesa")
    private Mesa mesa;
}