package com.daw.restauranteapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
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

/*  En este caso si usamos la query nativa en el JPA necesitamos pasar la fecha a LocalDate ya que
    va a recibir un Date desde la DB, linea 28
    // Constructor
    public ReservaDTO(Date fecha, String horaInicio, String horaFin, Integer numeroComensales,
                      String nombreCliente, String telefonoCliente, String emailCliente,
                      Long numeroMesa, String descripcionMesa) {
        this.fecha = fecha.toLocalDate();  // Convertimos el java.sql.Date a LocalDate
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.numeroComensales = numeroComensales;
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.emailCliente = emailCliente;
        this.numeroMesa = numeroMesa;
        this.descripcionMesa = descripcionMesa;
    }

    public ReservaDTO() {
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getNumeroComensales() {
        return numeroComensales;
    }

    public void setNumeroComensales(Integer numeroComensales) {
        this.numeroComensales = numeroComensales;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public Long getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Long numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getDescripcionMesa() {
        return descripcionMesa;
    }

    public void setDescripcionMesa(String descripcionMesa) {
        this.descripcionMesa = descripcionMesa;
    }*/
}


