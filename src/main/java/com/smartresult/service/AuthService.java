package com.smartresult.service;

import com.smartresult.dto.AuthDtos;
import com.smartresult.model.PasswordResetOtp;
import com.smartresult.model.User;
import com.smartresult.repository.PasswordResetOtpRepository;
import com.smartresult.repository.UserRepository;
import com.smartresult.security.JwtService;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private static final Logger log = LoggerFactory.getLogger(AuthService.class);
  private static final SecureRandom random = new SecureRandom();

  private final UserRepository userRepository;
  private final PasswordResetOtpRepository otpRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final JavaMailSender mailSender;
  private final long otpTtlMinutes;
  private final boolean logOtp;

  public AuthService(
      UserRepository userRepository,
      PasswordResetOtpRepository otpRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      JavaMailSender mailSender,
      @Value("${app.otp.ttlMinutes:10}") long otpTtlMinutes,
      @Value("${app.demo.logOtp:true}") boolean logOtp) {
    this.userRepository = userRepository;
    this.otpRepository = otpRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.mailSender = mailSender;
    this.otpTtlMinutes = otpTtlMinutes;
    this.logOtp = logOtp;
  }

  @Transactional
  public AuthDtos.LoginResponse register(AuthDtos.RegisterRequest req) {
    String email = req.email().trim().toLowerCase(Locale.ROOT);
    if (userRepository.existsByEmailIgnoreCase(email)) {
      throw new IllegalArgumentException("Email already registered");
    }

    User u = new User();
    u.setName(req.name().trim());
    u.setEmail(email);
    u.setRole(req.role());
    u.setPasswordHash(passwordEncoder.encode(req.password()));
    userRepository.save(u);

    String token = jwtService.generateAccessToken(u.getId(), u.getEmail(), u.getRole());
    return new AuthDtos.LoginResponse(token, u.getId(), u.getName(), u.getEmail(), u.getRole());
  }

  @Transactional(readOnly = true)
  public AuthDtos.LoginResponse login(AuthDtos.LoginRequest req) {
    String email = req.email().trim().toLowerCase(Locale.ROOT);
    User u = userRepository.findByEmailIgnoreCase(email)
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

    if (!u.isEnabled()) throw new IllegalArgumentException("Account disabled");
    if (u.getRole() != req.role()) throw new IllegalArgumentException("Role mismatch");
    if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    String token = jwtService.generateAccessToken(u.getId(), u.getEmail(), u.getRole());
    return new AuthDtos.LoginResponse(token, u.getId(), u.getName(), u.getEmail(), u.getRole());
  }

  @Transactional
  public void requestPasswordOtp(String emailRaw) {
    String email = emailRaw.trim().toLowerCase(Locale.ROOT);
    userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new IllegalArgumentException("Email not found"));

    otpRepository.deleteByExpiresAtBefore(Instant.now());

    String otp = String.format("%06d", random.nextInt(1_000_000));
    PasswordResetOtp entity = new PasswordResetOtp();
    entity.setEmail(email);
    entity.setOtp(otp);
    entity.setExpiresAt(Instant.now().plusSeconds(otpTtlMinutes * 60));
    otpRepository.save(entity);

    if (logOtp) log.info("DEV OTP for {} is {}", email, otp);

    // Send email (requires SMTP config). If SMTP isn't configured, this may throw at runtime.
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(email);
    msg.setSubject("SRADS Password Reset OTP");
    msg.setText("Your OTP is: " + otp + "\nThis OTP expires in " + otpTtlMinutes + " minutes.");
    mailSender.send(msg);
  }

  @Transactional
  public void resetPassword(AuthDtos.ResetPasswordRequest req) {
    String email = req.email().trim().toLowerCase(Locale.ROOT);
    User u = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new IllegalArgumentException("Email not found"));

    PasswordResetOtp latest = otpRepository
        .findTopByEmailIgnoreCaseAndUsedFalseOrderByCreatedAtDesc(email)
        .orElseThrow(() -> new IllegalArgumentException("OTP not requested"));

    if (latest.isUsed()) throw new IllegalArgumentException("OTP already used");
    if (latest.getExpiresAt().isBefore(Instant.now())) throw new IllegalArgumentException("OTP expired");
    if (!latest.getOtp().equals(req.otp().trim())) throw new IllegalArgumentException("Invalid OTP");

    latest.setUsed(true);
    otpRepository.save(latest);

    u.setPasswordHash(passwordEncoder.encode(req.newPassword()));
    userRepository.save(u);
  }
}

