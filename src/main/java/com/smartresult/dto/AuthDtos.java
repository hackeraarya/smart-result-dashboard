package com.smartresult.dto;

import com.smartresult.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
  public record RegisterRequest(
      @NotBlank(message = "Name is required")
      String name,
      @NotBlank(message = "Email is required")
      @Email(message = "Email should be valid")
      String email,
      @NotBlank(message = "Password is required")
      @Size(min = 6, message = "Password must be at least 6 characters")
      String password,
      Role role
  ) {}

  public record LoginRequest(
      @NotBlank(message = "Email is required")
      @Email(message = "Email should be valid")
      String email,
      @NotBlank(message = "Password is required")
      String password,
      Role role
  ) {}

  public record LoginResponse(
      String token,
      Long id,
      String name,
      String email,
      Role role
  ) {}

  public record ForgotRequest(
      @NotBlank(message = "Email is required")
      @Email(message = "Email should be valid")
      String email
  ) {}

  public record ResetPasswordRequest(
      @NotBlank(message = "Email is required")
      @Email(message = "Email should be valid")
      String email,
      @NotBlank(message = "OTP is required")
      String otp,
      @NotBlank(message = "Password is required")
      @Size(min = 6, message = "Password must be at least 6 characters")
      String newPassword
  ) {}

  public record ApiMessage(
      String message
  ) {}
}
