package com.chatop.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record RentalCreateRequest(@NotBlank String name,
                                  @NotNull @Positive BigDecimal surface,
                                  @NotNull @Positive BigDecimal price,
                                  @NotNull MultipartFile picture,
                                  @NotBlank String description) {}
