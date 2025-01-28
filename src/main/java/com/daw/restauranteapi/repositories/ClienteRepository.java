package com.daw.restauranteapi.repositories;

import com.daw.restauranteapi.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
