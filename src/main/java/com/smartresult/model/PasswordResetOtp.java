package com.smartresult.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "password_reset_otps", indexes = {
    @Index(name = "idx_otp_email", columnList = "email"),
    @Index(name = "idx_otp_expires", columnList = "expiresAt")
})
public class PasswordResetOtp {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 190)
  private String email;

  @Column(nullable = false, length = 12)
  private String otp;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(nullable = false)
  private boolean used = false;

  @Column(nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  public Long getId() { return id; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getOtp() { return otp; }
  public void setOtp(String otp) { this.otp = otp; }
  public Instant getExpiresAt() { return expiresAt; }
  public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
  public boolean isUsed() { return used; }
  public void setUsed(boolean used) { this.used = used; }
  public Instant getCreatedAt() { return createdAt; }
}

