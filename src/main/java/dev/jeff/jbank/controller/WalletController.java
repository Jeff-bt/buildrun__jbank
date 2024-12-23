package dev.jeff.jbank.controller;

import dev.jeff.jbank.controller.dto.CreateWalletDto;
import dev.jeff.jbank.controller.dto.DepositMoneyDto;
import dev.jeff.jbank.controller.dto.StatementDto;
import dev.jeff.jbank.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDto body) {
        var wallet = walletService.createWallet(body);

        return ResponseEntity.created(URI.create("/wallets/" + wallet.getWalletId().toString())).build();
    }

    @DeleteMapping(path = "{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable("walletId") UUID walletId) {
        Boolean isDeleted = walletService.deleteWallet(walletId);

        return isDeleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/{walletId}/deposits")
    public ResponseEntity<Void> depositMoney(@PathVariable("walletId") UUID walletId,
                                             @RequestBody @Valid DepositMoneyDto body,
                                             HttpServletRequest servletRequest) {
        walletService.depositMoney(
                walletId,
                body,
                servletRequest.getParameter("x-user-ip"));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{walletId}/statements")
    public ResponseEntity<StatementDto> getStatements(@PathVariable("walletId") UUID walletId,
                                                      @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(name = "OrderBy", defaultValue = "desc") String orderBy) {

        var statement = walletService.getStatements(walletId, page, pageSize);

        return ResponseEntity.ok(statement);
    }
}
