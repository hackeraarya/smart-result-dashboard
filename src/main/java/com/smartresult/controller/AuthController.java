package com.smartresult.controller;

import com.smartresult.dto.AuthDtos;
import com.smartresult.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthDtos.LoginResponse> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {
    return ResponseEntity.ok(authService.register(req));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthDtos.LoginResponse> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
    return ResponseEntity.ok(authService.login(req));
  }

  @PostMapping("/forgot/request-otp")
  public ResponseEntity<AuthDtos.ApiMessage> requestOtp(@Valid @RequestBody AuthDtos.ForgotRequest req) {
    authService.requestPasswordOtp(req.email());
    return ResponseEntity.ok(new AuthDtos.ApiMessage("OTP sent to email (check inbox/spam)."));
  }

  @PostMapping("/forgot/reset-password")
  public ResponseEntity<AuthDtos.ApiMessage> resetPassword(@Valid @RequestBody AuthDtos.ResetPasswordRequest req) {
    authService.resetPassword(req);
    return ResponseEntity.ok(new AuthDtos.ApiMessage("Password updated successfully."));
  }
}

