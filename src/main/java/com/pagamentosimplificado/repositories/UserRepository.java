package com.pagamentosimplificado.repositories;

import com.pagamentosimplificado.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByCnpjCpf(String cnpjCpf);

    Optional<User> findUserById(Long id);

}
