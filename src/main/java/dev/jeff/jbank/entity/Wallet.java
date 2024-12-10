package dev.jeff.jbank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_wallets")
public class Wallet {

    @Id
    @Column(name = "wallet_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletId;
    @Column(name = "cpf", unique = true)
    private String cpf;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "balance")
    private BigDecimal balance;

}