package com.pagamentosimplificado.services;

import com.pagamentosimplificado.domain.transaction.Transaction;
import com.pagamentosimplificado.domain.user.User;
import com.pagamentosimplificado.dtos.TransactionDTO;
import com.pagamentosimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public void createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizedTransaction(sender, transaction.value());
        if(!isAuthorized) {
            throw new Exception("Transaction not authorized");
        }

        // creating a transaction
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        // updating the balance
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        // persisting the data
        this.transactionRepository.save(newTransaction);
        this.userService.createUser(sender);
        this.userService.createUser(receiver);

    }

    public boolean authorizedTransaction(User sender, BigDecimal value) {

        ResponseEntity<AuthorizationResponse> response =
                restTemplate.getForEntity(authorizationServiceUrl, AuthorizationResponse.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            String message = response.getBody().message();
            return "Authorized".equals(message);
        } else return false;

    }


}
