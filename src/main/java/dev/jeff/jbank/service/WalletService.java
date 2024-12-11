package dev.jeff.jbank.service;

import dev.jeff.jbank.controller.dto.CreateWalletDto;
import dev.jeff.jbank.controller.dto.DepositMoneyDto;
import dev.jeff.jbank.entity.Deposit;
import dev.jeff.jbank.entity.Wallet;
import dev.jeff.jbank.exception.DeleteWalletException;
import dev.jeff.jbank.exception.WalletDataAlreadyExistsException;
import dev.jeff.jbank.exception.WalletNotFoundException;
import dev.jeff.jbank.repository.DepositRepository;
import dev.jeff.jbank.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;


    public WalletService(WalletRepository walletRepository, DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        if (walletRepository.existsByCpfOrEmailAllIgnoreCase(dto.cpf(), dto.email())) {
            throw new WalletDataAlreadyExistsException("cpf or email already exists");
        }

        var wallet = new Wallet();
        wallet.setName(dto.name());
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }

    public Boolean deleteWallet(UUID walletId) {
        var wallet = walletRepository.findById(walletId);
        if (wallet.isPresent()) {

            if(wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0){
                throw new DeleteWalletException("The balance is not zero. The current amount is $" + wallet.get().getBalance());
            }

            walletRepository.deleteById(walletId);
        }

        return wallet.isPresent();
    }

    @Transactional
    public void depositMoney(UUID walletId, DepositMoneyDto dto, String ipAddress) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("There is not wallet with this id"));

        var deposit = new Deposit();
        deposit.setDepositValue(dto.value());
        deposit.setWallet(wallet);
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        wallet.setBalance(wallet.getBalance().add(dto.value()));

        walletRepository.save(wallet);
        depositRepository.save(deposit);
    }
}
