package org.featherlessbipeds.exception;

import lombok.Getter;
import org.featherlessbipeds.utils.ClientFlag;

@Getter
public class EditProfileException extends Exception
{
    private final ClientFlag flag;

    public EditProfileException(ClientFlag flag)
    {
        super("Profile edit failed: " + flag);
        this.flag = flag;
    }
}