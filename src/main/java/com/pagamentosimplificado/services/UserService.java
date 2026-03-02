package com.pagamentosimplificado.services;

import com.pagamentosimplificado.domain.user.User;
import com.pagamentosimplificado.domain.user.UserType;
import com.pagamentosimplificado.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void validateTransaction(User sender, BigDecimal amount) throws Exception {

        if(sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("User is not authorized to perform this transaction");
        }

        if(sender.getBalance().compareTo(amount) <= 0) {
            throw new Exception("Insufficient balance");
        }

    }

    public User findUserById(long id) throws Exception {
        return this.userRepository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

}
