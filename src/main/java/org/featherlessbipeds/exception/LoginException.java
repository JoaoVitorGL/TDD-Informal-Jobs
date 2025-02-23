package org.featherlessbipeds.exception;

import lombok.Getter;
import org.featherlessbipeds.utils.UserFlag;

@Getter
public class LoginException extends Exception
{
    private final UserFlag flag;

    public LoginException(UserFlag flag)
    {
        super("Login failed: " + flag);
        this.flag = flag;
    }
}