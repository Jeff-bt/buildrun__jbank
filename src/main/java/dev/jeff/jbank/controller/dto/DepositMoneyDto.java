package dev.jeff.jbank.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositMoneyDto(@NotNull @DecimalMin("1.00") BigDecimal value) {
}
