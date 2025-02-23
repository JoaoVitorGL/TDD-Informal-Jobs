package org.featherlessbipeds.service;

import org.featherlessbipeds.exception.ChangePasswordException;
import org.featherlessbipeds.exception.LoginException;
import org.featherlessbipeds.model.User;
import org.featherlessbipeds.repository.contracts.UserRepository;
import org.featherlessbipeds.service.contracts.AppTokenService;
import org.featherlessbipeds.service.contracts.FacebookAuthService;
import org.featherlessbipeds.service.contracts.GoogleAuthService;
import org.featherlessbipeds.utils.PasswordUserFlag;
import org.featherlessbipeds.utils.UserFlag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @Mock
    private UserRepository repository;
    @Mock
    private GoogleAuthService googleAuthService;
    @Mock
    private FacebookAuthService facebookAuthService;
    @Mock
    private AppTokenService appTokenService;
    @InjectMocks
    private UserService service;

    private void usualAssertRoutine(User c1, User c2)
    {
        assertNotNull(c1);
        assertNotNull(c2);
        assertEquals(c1.getEmail(), c2.getEmail());
        assertEquals(c1.getPassword(), c2.getPassword());
    }

    @Test
    public void tc01_login_ReturnsUser_WhenSuccessful() throws LoginException
    {
        var u = User.builder().email("usuario@somemail.com").password("123456@f").build();
        when(repository.findByEmailAndPassword(u.getEmail(), u.getPassword()))
                .thenReturn(Optional.of(u));

        User returnedUser = service.login(u);

        usualAssertRoutine(u, returnedUser);
        verify(repository, times(1)).findByEmailAndPassword(u.getEmail(), u.getPassword());
    }

    @Test
    public void tc02_login_ThrowsException_WhenPasswordIsInvalid()
    {
        var u = User.builder().email("usuario@somemail.com").password("123").build();
        when(repository.findByEmailAndPassword(u.getEmail(), u.getPassword()))
                .thenReturn(Optional.empty());

        LoginException exception = assertThrows(LoginException.class, () -> service.login(u));

        assertEquals(UserFlag.LOGIN_ERROR, exception.getFlag());
        verify(repository, times(1)).findByEmailAndPassword(u.getEmail(), u.getPassword());
    }

    @Test
    public void tc03_login_ThrowsException_WhenEmailIsInvalid()
    {
        var u = User.builder().email("usuario999@somemail.com").password("123456@f").build();
        when(repository.findByEmailAndPassword(u.getEmail(), u.getPassword()))
                .thenReturn(Optional.empty());

        LoginException exception = assertThrows(LoginException.class, () -> service.login(u));

        assertEquals(UserFlag.LOGIN_ERROR, exception.getFlag());
        verify(repository, times(1)).findByEmailAndPassword(u.getEmail(), u.getPassword());
    }

    @Test
    public void tc04_googleLogin_ReturnsUser_WhenSuccessful() throws LoginException
    {
        var u = User.builder().email("googleuser@somemail.com").build();
        String jwtToken = "mocked-JWT-token";

        when(googleAuthService.verifyToken(jwtToken))
                .thenReturn(Optional.of(u));
        when(repository.findByEmail(u.getEmail()))
                .thenReturn(Optional.of(u));

        User returnedUser = service.googleLogin(jwtToken);

        usualAssertRoutine(u, returnedUser);
        verify(googleAuthService, times(1)).verifyToken(jwtToken);
        verify(repository, times(1)).findByEmail(u.getEmail());
    }

    @Test
    public void tc05_googleLogin_ThrowsException_WhenAuthFails()
    {
        String jwtToken = "mocked-INVALID-JWT-token";

        when(googleAuthService.verifyToken(jwtToken))
                .thenReturn(Optional.empty());

        LoginException exception = assertThrows(LoginException.class, () -> service.googleLogin(jwtToken));

        assertEquals(UserFlag.GOOGLE_LOGIN_ERROR, exception.getFlag());
        verify(googleAuthService, times(1)).verifyToken(jwtToken);
        verify(repository, never()).findByEmail(any());
    }

    @Test
    public void tc06_facebookLogin_ReturnsUser_WhenSuccessful() throws LoginException
    {
        var u = User.builder().email("face@somemail.com").build();
        String jwtToken = "mocked-JWT-token";

        when(facebookAuthService.verifyToken(jwtToken))
                .thenReturn(Optional.of(u));
        when(repository.findByEmail(u.getEmail()))
                .thenReturn(Optional.of(u));

        User returnedUser = service.facebookLogin(jwtToken);
        usualAssertRoutine(u, returnedUser);

        verify(facebookAuthService, times(1)).verifyToken(jwtToken);
        verify(repository, times(1)).findByEmail(u.getEmail());
    }

    @Test
    public void tc07_facebookLogin_ThrowsException_WhenAuthFails()
    {
        String jwtToken = "mocked-INVALID-JWT-token";

        when(facebookAuthService.verifyToken(jwtToken))
                .thenReturn(Optional.empty());

        LoginException exception = assertThrows(LoginException.class, () -> service.facebookLogin(jwtToken));

        assertEquals(UserFlag.FACEBOOK_LOGIN_ERROR, exception.getFlag());
        verify(facebookAuthService, times(1)).verifyToken(jwtToken);
        verify(repository, never()).findByEmail(any());
    }

    @Test
    public void tc08_changePassword_ReturnsUser_WhenSuccessful() throws ChangePasswordException
    {
        var u = User.builder().email("usuario@somemail.com").build();
        var newPwd = "000000";
        var newPwdConfirm = "000000";
        String appToken = "505050";

        when(appTokenService.verifyToken(appToken))
                .thenReturn(PasswordUserFlag.VALID_TOKEN);
        when(repository.findByEmail(u.getEmail()))
                .thenReturn(Optional.of(u));
        when(repository.updatePassword(u.getEmail(), newPwd))
                .thenReturn(true);

        User returnedUser = service.changePassword(u, appToken, newPwd, newPwdConfirm);

        usualAssertRoutine(u, returnedUser);
        verify(appTokenService, times(1)).verifyToken(appToken);
        verify(repository, times(1)).findByEmail(u.getEmail());
        verify(repository, times(1)).updatePassword(u.getEmail(), newPwd);
    }

    @Test
    public void tc09_changePassword_ThrowsException_WhenTokenIsInvalid()
    {
        var u = User.builder().email("usuario@somemail.com").build();
        var newPwd = "000000";
        var newPwdConfirm = "000000";
        String appToken = "515151";

        when(appTokenService.verifyToken(appToken))
                .thenReturn(PasswordUserFlag.INVALID_TOKEN);

        ChangePasswordException exception = assertThrows(ChangePasswordException.class, () -> service.changePassword(u, appToken, newPwd, newPwdConfirm));

        assertEquals(UserFlag.CHANGE_PASSWORD_INVALID_TOKEN, exception.getFlag());
        verify(appTokenService, times(1)).verifyToken(appToken);
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).updatePassword(any(), any());
    }

    @Test
    public void tc10_changePassword_ThrowsException_WhenNewPwdConfirmIsInvalid()
    {
        var u = User.builder().email("usuario@somemail.com").build();
        var newPwd = "000000";
        var newPwdConfirm = "000001";
        String appToken = "505050";

        when(appTokenService.verifyToken(appToken))
                .thenReturn(PasswordUserFlag.VALID_TOKEN);

        ChangePasswordException exception = assertThrows(ChangePasswordException.class, () -> service.changePassword(u, appToken, newPwd, newPwdConfirm));

        assertEquals(UserFlag.CHANGE_PASSWORD_INVALID_CONFIRM, exception.getFlag());
        verify(appTokenService, times(1)).verifyToken(appToken);
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).updatePassword(any(), any());
    }

    @Test
    public void tc11_changePassword_ThrowsException_WhenUserIsNotFound()
    {
        var u = User.builder().email("dsadsadsa@somemail.com").build();
        var newPwd = "000000";
        var newPwdConfirm = "000000";
        String appToken = "505050";

        when(appTokenService.verifyToken(appToken))
                .thenReturn(PasswordUserFlag.VALID_TOKEN);
        when(repository.findByEmail(u.getEmail()))
                .thenReturn(Optional.empty());

        ChangePasswordException exception = assertThrows(ChangePasswordException.class, () -> service.changePassword(u, appToken, newPwd, newPwdConfirm));

        assertEquals(UserFlag.CHANGE_PASSWORD_USER_NOT_FOUND, exception.getFlag());
        verify(appTokenService, times(1)).verifyToken(appToken);
        verify(repository, times(1)).findByEmail(u.getEmail());
        verify(repository, never()).updatePassword(any(), any());
    }
}