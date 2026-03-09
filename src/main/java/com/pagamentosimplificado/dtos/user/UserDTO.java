package com.pagamentosimplificado.dtos.user;

import com.pagamentosimplificado.domain.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UserDTO(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String cnpjCpf,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password,

        BigDecimal balance,

        @NotNull
        UserType userType
) {
}
