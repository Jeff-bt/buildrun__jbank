package dev.jeff.jbank.controller.dto;

import java.util.List;

public record StatementDto(WalletDto dto,
                           List<StatementItemDto> statements,
                           PaginationDto pagination) {
}
