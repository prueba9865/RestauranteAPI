package com.daw.restauranteapi.repositories;

import com.daw.restauranteapi.entities.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
}
