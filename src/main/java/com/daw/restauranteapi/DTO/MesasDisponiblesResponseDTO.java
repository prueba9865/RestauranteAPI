package com.daw.restauranteapi.DTO;

import com.daw.restauranteapi.entities.Mesa;

import java.util.List;

public class MesasDisponiblesResponseDTO {
    private boolean success;
    private String message;
    private List<Mesa> mesas;

    // Constructor, getters y setters
    public MesasDisponiblesResponseDTO(boolean success, String message, List<Mesa> mesas) {
        this.success = success;
        this.message = message;
        this.mesas = mesas;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }
}