package dev.jeff.jbank.service;

import dev.jeff.jbank.controller.dto.*;
import dev.jeff.jbank.entity.Deposit;
import dev.jeff.jbank.entity.Wallet;
import dev.jeff.jbank.exception.DeleteWalletException;
import dev.jeff.jbank.exception.StatementException;
import dev.jeff.jbank.exception.WalletDataAlreadyExistsException;
import dev.jeff.jbank.exception.WalletNotFoundException;
import dev.jeff.jbank.repository.DepositRepository;
import dev.jeff.jbank.repository.WalletRepository;
import dev.jeff.jbank.repository.dto.StatementView;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public StatementDto getStatements(UUID walletId, Integer page, Integer pageSize) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("There is not wallet with this id"));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

        var statements = walletRepository.findStatements(walletId.toString(), pageRequest)
                .map(view -> mapToDto(walletId, view));


        return new StatementDto(
                new WalletDto(wallet.getWalletId(), wallet.getCpf(), wallet.getEmail(), wallet.getName(), wallet.getBalance()),
                statements.getContent(),
                new PaginationDto(statements.getNumber(), statements.getSize(), statements.getTotalElements(),statements.getTotalPages())
        );
    }

    private StatementItemDto mapToDto(UUID walletId, StatementView view) {

        if (view.getType().equalsIgnoreCase("deposit")){
            return mapToDeposit(view);
        }

        if (view.getType().equalsIgnoreCase("transfer")
        && view.getWalletSender().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferSend(walletId, view);
        }

        if (view.getType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferReceived(walletId, view);
        }

        throw new StatementException("invalid type " + view.getType());
    }

    private StatementItemDto mapWhenTransferReceived(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money received from " + view.getWalletSender(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private StatementItemDto mapWhenTransferSend(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money sent to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.DEBIT
        );
    }

    private static StatementItemDto mapToDeposit(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }
}
