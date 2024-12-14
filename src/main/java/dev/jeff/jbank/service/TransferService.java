package dev.jeff.jbank.service;

import dev.jeff.jbank.controller.dto.TransferMoneyDto;
import dev.jeff.jbank.entity.Transfer;
import dev.jeff.jbank.entity.Wallet;
import dev.jeff.jbank.exception.TransferException;
import dev.jeff.jbank.exception.WalletNotFoundException;
import dev.jeff.jbank.repository.TransferRepository;
import dev.jeff.jbank.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(TransferMoneyDto dto) {

        var sender = walletRepository.findById(dto.senderId())
                .orElseThrow(() -> new WalletNotFoundException("sender does not exist"));
        var receiver = walletRepository.findById(dto.receiverId())
                .orElseThrow(() -> new WalletNotFoundException("receiver does not exist"));

        if (sender.getBalance().compareTo(dto.transferValue()) == -1) {
            throw new TransferException("insufficient balance. you current balance is $" + sender.getBalance());
        }

        persistTransfer(dto, sender, receiver);
        updateWallets(dto, sender, receiver);

    }

    private void updateWallets(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.transferValue()));
        receiver.setBalance(sender.getBalance().add(dto.transferValue()));
        walletRepository.save(sender);
        walletRepository.save(receiver);
    }

    private void persistTransfer(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        var transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setReceiver(receiver);
        transfer.setTransferValue(dto.transferValue());
        transfer.setTransferDateTime(LocalDateTime.now());

        transferRepository.save(transfer);
    }
}
