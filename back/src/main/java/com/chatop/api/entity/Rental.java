package com.chatop.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RENTALS")
public class Rental {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private BigDecimal surface;

  private BigDecimal price;

  private String picture;

  @Column(length = 2000)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  protected Rental() {}

  public Rental(String name, BigDecimal surface, BigDecimal price, String picture, String description, User owner) {
    this.name = name;
    this.surface = surface;
    this.price = price;
    this.picture = picture;
    this.description = description;
    this.owner = owner;
  }

  public void update(String name, BigDecimal surface, BigDecimal price, String description) {
    this.name = name;
    this.surface = surface;
    this.price = price;
    this.description = description;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getSurface() {
    return surface;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getPicture() {
    return picture;
  }

  public String getDescription() {
    return description;
  }

  public User getOwner() {
    return owner;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}
