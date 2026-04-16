package com.smartresult.controller;

import com.smartresult.security.JwtPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class DemoProtectedController {
  @GetMapping
  public JwtPrincipal me(Authentication auth) {
    return (JwtPrincipal) auth.getPrincipal();
  }
}

