package com.pagamentosimplificado.services;

import com.pagamentosimplificado.domain.transaction.Transaction;
import com.pagamentosimplificado.domain.user.User;
import com.pagamentosimplificado.dtos.authorization.AuthorizationResponseDTO;
import com.pagamentosimplificado.dtos.transaction.TransactionDTO;
import com.pagamentosimplificado.exceptions.BusinessException;
import com.pagamentosimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final UserService userService;

    private final TransactionRepository transactionRepository;

    private final RestTemplate restTemplate;

    @Value("${authorization.service.url}")
    private String authorizationServiceUrl;

    public TransactionService(
            UserService userService,
            TransactionRepository transactionRepository,
            RestTemplate restTemplate
            ) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

@Transactional
    public void createTransaction(TransactionDTO transaction) {


        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());


        if(!isAuthorizedTransaction()) {
            throw new BusinessException("Transaction not authorized");
        }

        // creating a transaction
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        // updating the balance
        sender.debit(transaction.value());
        receiver.credit(transaction.value());

        // persisting the data
        transactionRepository.save(newTransaction);

    }

    public boolean isAuthorizedTransaction() {

        ResponseEntity<AuthorizationResponseDTO> response =
                restTemplate.getForEntity(
                        authorizationServiceUrl,
                        AuthorizationResponseDTO.class
                );

        return response.getStatusCode().is2xxSuccessful()
            && response.getBody() != null
            && "success".equals(response.getBody().status())
            && response.getBody().data() != null
            && response.getBody().data().authorization();

    }

}
