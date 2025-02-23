package org.featherlessbipeds.exception;

import lombok.Getter;
import org.featherlessbipeds.utils.UserFlag;

@Getter
public class ChangePasswordException extends Exception
{
    private final UserFlag flag;

    public ChangePasswordException(UserFlag flag)
    {
        super("Password change failed: " + flag);
        this.flag = flag;
    }
}