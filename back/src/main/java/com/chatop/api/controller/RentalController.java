package com.chatop.api.controller;

import com.chatop.api.dto.*;
import com.chatop.api.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Locations", description = "Consultation, création et modification des locations")
public class RentalController {

  private final RentalService rentalService;

  public RentalController(RentalService rentalService) {
    this.rentalService = rentalService;
  }

  @GetMapping
  @Operation(summary = "Lister les locations")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Liste des locations"),
    @ApiResponse(responseCode = "401", description = "Token absent, invalide ou expiré", content = @Content)
  })
  public ResponseEntity<RentalsResponse> getRentals() {
    return ResponseEntity.ok(rentalService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Détail d'une location")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Location trouvée"),
    @ApiResponse(responseCode = "401", description = "Token absent, invalide ou expiré", content = @Content),
    @ApiResponse(responseCode = "404", description = "Location introuvable",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<RentalResponse> getRental(@PathVariable Integer id) {
    return ResponseEntity.ok(rentalService.getById(id));
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Créer une location",
    description = "Formulaire multipart. L'image est stockée sur le serveur et son URL est enregistrée. Le propriétaire est l'utilisateur connecté.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Location créée"),
    @ApiResponse(responseCode = "400", description = "Champ manquant ou invalide, fichier non image",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "401", description = "Token absent, invalide ou expiré", content = @Content)
  })
  public ResponseEntity<MessageResponse> createRental(@Valid @ModelAttribute RentalCreateRequest request,
                                                      Authentication authentication) {
    rentalService.create(request, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Rental created !"));
  }

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Modifier une location",
    description = "Formulaire multipart sans image (l'image existante est conservée). Réservé au propriétaire.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Location modifiée"),
    @ApiResponse(responseCode = "400", description = "Champ manquant ou invalide",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "401", description = "Token absent, invalide ou expiré", content = @Content),
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'est pas le propriétaire",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "404", description = "Location introuvable",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<MessageResponse> updateRental(@PathVariable Integer id,
                                                      @Valid @ModelAttribute RentalUpdateRequest request,
                                                      Authentication authentication) {
    rentalService.update(id, request, authentication.getName());
    return ResponseEntity.ok(new MessageResponse("Rental updated !"));
  }
}
