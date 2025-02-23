package org.featherlessbipeds.service.contracts;

import org.featherlessbipeds.utils.PasswordUserFlag;

public interface AppTokenService
{
    PasswordUserFlag verifyToken(String appToken);
}
