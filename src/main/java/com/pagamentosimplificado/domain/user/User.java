package com.pagamentosimplificado.domain.user;

import jakarta.persistence.*;
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

}
