package dev.jeff.jbank.service;

import dev.jeff.jbank.controller.dto.CreateWalletDto;
import dev.jeff.jbank.entity.Wallet;
import dev.jeff.jbank.exception.WalletDataAlreadyExistsException;
import dev.jeff.jbank.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        if (walletRepository.existsByCpfAndEmailAllIgnoreCase(dto.cpf(), dto.email())) {
            throw new WalletDataAlreadyExistsException("cpf ir email already exists");
        }

        var wallet = new Wallet();
        wallet.setName(dto.name());
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }
}
