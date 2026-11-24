package com.chatop.api.service;

import com.chatop.api.controller.dto.rental.RentalCreateRequest;
import com.chatop.api.controller.dto.rental.RentalResponse;
import com.chatop.api.controller.dto.rental.RentalUpdateRequest;
import com.chatop.api.controller.dto.rental.RentalsResponse;
import com.chatop.api.entity.Rental;
import com.chatop.api.entity.User;
import com.chatop.api.exception.ResourceNotFoundException;
import com.chatop.api.repository.RentalRepository;
import com.chatop.api.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentalService {

  private final RentalRepository rentalRepository;
  private final UserRepository userRepository;
  private final FileStorageService fileStorageService;

  public RentalService(RentalRepository rentalRepository,
                       UserRepository userRepository,
                       FileStorageService fileStorageService) {
    this.rentalRepository = rentalRepository;
    this.userRepository = userRepository;
    this.fileStorageService = fileStorageService;
  }

  @Transactional(readOnly = true)
  public RentalsResponse getAll() {
    return new RentalsResponse(rentalRepository.findAll().stream()
      .map(RentalResponse::from)
      .toList());
  }

  @Transactional(readOnly = true)
  public RentalResponse getById(Integer id) {
    return RentalResponse.from(findRental(id));
  }

  @Transactional
  public void create(RentalCreateRequest request, String ownerEmail) {
    User owner = userRepository.findByEmail(ownerEmail)
      .orElseThrow(() -> new ResourceNotFoundException("User not found: " + ownerEmail));
    String pictureUrl = fileStorageService.store(request.picture());
    rentalRepository.save(new Rental(
      request.name(),
      request.surface(),
      request.price(),
      pictureUrl,
      request.description(),
      owner));
  }

  @Transactional
  public void update(Integer id, RentalUpdateRequest request, String userEmail) {
    Rental rental = findRental(id);
    if (!rental.getOwner().getEmail().equals(userEmail)) {
      throw new AccessDeniedException("Only the owner can update this rental");
    }
    rental.update(request.name(), request.surface(), request.price(), request.description());
  }

  private Rental findRental(Integer id) {
    return rentalRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Rental not found: " + id));
  }
}
