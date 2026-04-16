package com.smartresult.security;

import com.smartresult.model.Role;

public record JwtPrincipal(Long userId, String email, Role role) {}

