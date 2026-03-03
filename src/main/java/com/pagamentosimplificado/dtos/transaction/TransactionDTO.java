package com.pagamentosimplificado.dtos.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO (
        @NotBlank
        @DecimalMin("0.01")
        BigDecimal value,

        @NotNull
        Long senderId,

        @NotNull
        Long receiverId)
{}
