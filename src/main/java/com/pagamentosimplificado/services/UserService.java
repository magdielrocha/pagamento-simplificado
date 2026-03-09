package com.pagamentosimplificado.services;

import com.pagamentosimplificado.domain.user.User;
import com.pagamentosimplificado.domain.user.UserType;
import com.pagamentosimplificado.dtos.user.UserDTO;
import com.pagamentosimplificado.exceptions.BusinessException;
import com.pagamentosimplificado.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
    }

    public User createUser(@Valid UserDTO data) {
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }


}
