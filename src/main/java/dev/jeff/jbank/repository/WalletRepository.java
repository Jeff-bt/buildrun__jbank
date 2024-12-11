package dev.jeff.jbank.repository;

import dev.jeff.jbank.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {


    Boolean existsByCpfAndEmailAllIgnoreCase(String cpf, String email);
}
