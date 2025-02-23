package org.featherlessbipeds.repository.contracts;

import org.featherlessbipeds.model.User;

import java.util.Optional;

public interface UserRepository
{
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);

    Boolean updatePassword(String email, String newPwd);
}
