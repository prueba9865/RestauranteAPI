package com.daw.restauranteapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @FutureOrPresent(message = "La fecha no puede haberse pasado")
    @NotNull(message = "Introduce la fecha de la reserva")
    private LocalDate fecha;
    @NotBlank(message = "La hora de inicio no puede estar vacia")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$", message = "La hora debe tener el formato hh:mm (24 horas).")
    private String horaInicio;
    @NotBlank(message = "La hora de fin no puede estar vacia")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$", message = "La hora debe tener el formato hh:mm (24 horas).")
    private String horaFin;
    @Min(value = 0, message = "El numero de comensales no puede ser negativo")
    @NotNull(message = "El numero de comensales no puede estar vacio")
    private Integer numeroComensales;

    @ManyToOne(targetEntity = Cliente.class)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(targetEntity = Mesa.class)
    @JoinColumn(name = "id_mesa")
    private Mesa mesa;
}