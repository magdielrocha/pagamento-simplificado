package com.pagamentosimplificado.dtos.authorization;

public record AuthorizationResponseDTO(
        String status,
        AuthorizationData data
) {
}
