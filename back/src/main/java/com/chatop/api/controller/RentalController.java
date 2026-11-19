package com.chatop.api.controller;

import com.chatop.api.dto.*;
import com.chatop.api.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

  private final RentalService rentalService;

  public RentalController(RentalService rentalService) {
    this.rentalService = rentalService;
  }

  @GetMapping
  public ResponseEntity<RentalsResponse> getRentals() {
    return ResponseEntity.ok(rentalService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<RentalResponse> getRental(@PathVariable Integer id) {
    return ResponseEntity.ok(rentalService.getById(id));
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> createRental(@Valid @ModelAttribute RentalCreateRequest request,
                                                      Authentication authentication) {
    rentalService.create(request, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Rental created !"));
  }

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> updateRental(@PathVariable Integer id,
                                                      @Valid @ModelAttribute RentalUpdateRequest request,
                                                      Authentication authentication) {
    rentalService.update(id, request, authentication.getName());
    return ResponseEntity.ok(new MessageResponse("Rental updated !"));
  }
}
