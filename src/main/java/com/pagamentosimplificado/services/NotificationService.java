package com.pagamentosimplificado.services;

import com.pagamentosimplificado.domain.user.User;
import com.pagamentosimplificado.dtos.notification.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);


    private final RestTemplate restTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;


    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void sendNotification(User user, String message) {

        try {

            NotificationDTO notificationRequest =
                    new NotificationDTO(user.getEmail(), message);

            ResponseEntity<Void> response =
                    restTemplate.postForEntity(
                            notificationServiceUrl,
                            notificationRequest,
                            Void.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error(
                        "Notification failed. Status: {} | Error: {}",
                        response.getStatusCode(),
                        user.getEmail()
                );
            }
        } catch (Exception ex) {
            logger.error(
                    "Notification service unavailable. Email: {} | Error: {}",
                    user.getEmail(),
                    ex.getMessage(),
                    ex
            );
        }
    }
}
