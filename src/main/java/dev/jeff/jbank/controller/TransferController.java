package dev.jeff.jbank.controller;

import dev.jeff.jbank.controller.dto.TransferMoneyDto;
import dev.jeff.jbank.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferMoneyDto body) {
        transferService.transferMoney(body);

        return ResponseEntity.ok().build();
    }
}
