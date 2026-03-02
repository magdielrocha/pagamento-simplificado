package com.pagamentosimplificado.services;

public record NotificationRequest(
        String email,
        String message) {
}
