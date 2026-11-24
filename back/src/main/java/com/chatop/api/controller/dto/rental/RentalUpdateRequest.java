package com.chatop.api.controller.dto.rental;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RentalUpdateRequest(@NotBlank String name,
                                  @NotNull @Positive BigDecimal surface,
                                  @NotNull @Positive BigDecimal price,
                                  @NotNull String description) {
}
