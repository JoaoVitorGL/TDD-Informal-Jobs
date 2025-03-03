package org.featherlessbipeds.exception;

import lombok.Getter;
import org.featherlessbipeds.utils.EditProfileFlag;

@Getter
public class EditProfileException extends Exception
{
    private final EditProfileFlag flag;

    public EditProfileException(EditProfileFlag flag)
    {
        super("Profile edit failed: " + flag);
        this.flag = flag;
    }
}