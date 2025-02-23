package org.featherlessbipeds.service.contracts;

import org.featherlessbipeds.model.User;

import java.util.Optional;

public interface GoogleAuthService
{
    Optional<User> verifyToken(String jwtToken);
}
