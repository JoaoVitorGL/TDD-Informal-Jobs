package org.featherlessbipeds.service;

import lombok.RequiredArgsConstructor;
import org.featherlessbipeds.model.User;
import org.featherlessbipeds.repository.contracts.UserRepository;
import org.featherlessbipeds.service.contracts.AppTokenService;
import org.featherlessbipeds.service.contracts.FacebookAuthService;
import org.featherlessbipeds.service.contracts.GoogleAuthService;
import org.featherlessbipeds.utils.PasswordUserFlag;
import org.featherlessbipeds.utils.UserFlag;
import org.featherlessbipeds.exception.LoginException;
import org.featherlessbipeds.exception.ChangePasswordException;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final GoogleAuthService googleAuthService;
    private final FacebookAuthService facebookAuthService;
    private final AppTokenService appTokenService;

    public User login(User user) throws LoginException
    {
        Optional<User> foundUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (foundUser.isEmpty())
        {
            throw new LoginException(UserFlag.LOGIN_ERROR);
        }
        return foundUser.get();
    }

    public User googleLogin(String jwtToken) throws LoginException
    {
        Optional<User> googleUser = googleAuthService.verifyToken(jwtToken);
        if (googleUser.isEmpty())
        {
            throw new LoginException(UserFlag.GOOGLE_LOGIN_ERROR);
        }

        Optional<User> foundUser = userRepository.findByEmail(googleUser.get().getEmail());
        if (foundUser.isEmpty())
        {
            throw new LoginException(UserFlag.GOOGLE_LOGIN_ERROR);
        }

        return foundUser.get();
    }

    public User facebookLogin(String jwtToken) throws LoginException
    {
        Optional<User> facebookUser = facebookAuthService.verifyToken(jwtToken);
        if (facebookUser.isEmpty())
        {
            throw new LoginException(UserFlag.FACEBOOK_LOGIN_ERROR);
        }

        Optional<User> foundUser = userRepository.findByEmail(facebookUser.get().getEmail());
        if (foundUser.isEmpty())
        {
            throw new LoginException(UserFlag.FACEBOOK_LOGIN_ERROR);
        }

        return foundUser.get();
    }

    public User changePassword(User user, String appToken, String newPassword, String newPasswordConfirm)
            throws ChangePasswordException
    {
        PasswordUserFlag tokenFlag = appTokenService.verifyToken(appToken);
        if (tokenFlag != PasswordUserFlag.VALID_TOKEN)
            throw new ChangePasswordException(UserFlag.CHANGE_PASSWORD_INVALID_TOKEN);

        if (!newPassword.equals(newPasswordConfirm))
            throw new ChangePasswordException(UserFlag.CHANGE_PASSWORD_INVALID_CONFIRM);

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser.isEmpty())
            throw new ChangePasswordException(UserFlag.CHANGE_PASSWORD_USER_NOT_FOUND);

        boolean ok = userRepository.updatePassword(user.getEmail(), newPassword);
        if (!ok)
            throw new ChangePasswordException(UserFlag.CHANGE_PASSWORD_ERROR);

        return foundUser.get();
    }
}