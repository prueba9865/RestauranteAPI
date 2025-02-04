package com.daw.restauranteapi.DTO;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RoleAssignmentDTO {
    private String username;
    private String role;
}