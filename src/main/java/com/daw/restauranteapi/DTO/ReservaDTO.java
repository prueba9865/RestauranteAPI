package com.daw.restauranteapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private LocalDate fecha;
    private String horaInicio;
    private String horaFin;
    private Integer numeroComensales;
    private String nombreCliente;
    private String telefonoCliente;
    private String emailCliente;
    private Long numeroMesa;
    private String descripcionMesa;
}


