package com.pagamentosimplificado.domain.user;

import com.pagamentosimplificado.dtos.user.UserDTO;
import com.pagamentosimplificado.exceptions.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity(name="users")
@Table(name="tb_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable=false)
    private String firstName;

    @NotBlank
    @Column(nullable=false)
    private String lastName;

    @NotBlank
    @Column(nullable=false,  unique=true)
    private String cnpjCpf;

    @NotBlank
    @Email
    @Column(nullable=false,  unique=true)
    private String email;

    @NotBlank
    @Column(nullable=false)
    private String password;

    @NotNull
    @Column(nullable=false)
    @PositiveOrZero
    private BigDecimal balance =  BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(@Valid UserDTO data) {
        this.firstName = data.firstName();
        this.lastName = data.lastName();
        this.cnpjCpf = data.cnpjCpf();
        this.email = data.email();
        this.password = data.password();
        this.userType = data.userType();
    }


    public void debit(BigDecimal amount) {

        if(this.getUserType() == UserType.MERCHANT) {
            throw new BusinessException("User is not authorized to perform this transaction");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Invalid amount");
        }

        if(this.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient balance");
        }

        this.balance = this.balance.subtract(amount);

    }


    public void credit(BigDecimal amount){

        this.balance = this.balance.add(amount);

    }

}
