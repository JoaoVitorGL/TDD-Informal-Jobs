package org.featherlessbipeds.service;

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

    @Test
    public void tc01_login_ReturnsSuccessFlag_WhenSuccessful()
    {
        var u = User.builder().email("usuario@somemail.com").password("123456@f").build();
        when(repository.findByEmailAndPassword(u.getEmail(), u.getPassword()))
                .thenReturn(Optional.of(u));

        Flag flag = service.login(u);
        assertEquals(UserFlag.LOGIN_SUCCESS, flag);
    }

    @Test
    public void tc02_login_ReturnsErrorFlag_WhenPasswordIsInvalid()
    {
        var u = User.builder().email("usuario@somemail.com").password("123").build();
        when(repository.findByEmailAndPassword(u.getEmail(), u.getPassword()))
                .thenReturn(Optional.empty());

        Flag flag = service.login(u);

        assertEquals(UserFlag.LOGIN_ERROR, flag);
    }

    @Test
    public void tc03_login_ReturnsErrorFlag_WhenEmailIsInvalid()
    {
        var u = User.builder().email("usuario999@somemail.com").password("123456@f").build();
        when(repository.findByEmailAndPassword(u.getEmail(), u.getPassword()))
                .thenReturn(Optional.empty());

        Flag flag = service.login(u);

        assertEquals(UserFlag.LOGIN_ERROR, flag);
    }

    @Test
    public void tc04_googleLogin_ReturnsGoogleSuccessFlag_WhenSuccessful() {
        var u = User.builder().email("googleuser@somemail.com").build();
        String jwtToken = "mocked-JWT-token";

        when(googleAuthService.verifyToken(jwtToken))
                .thenReturn(Optional.of(u));
        when(repository.findByEmail(u.getEmail()))
                .thenReturn(Optional.of(u));

        Flag flag = service.googleLogin(jwtToken);

        assertEquals(UserFlag.GOOGLE_LOGIN_SUCCESS, flag);
    }

    @Test
    public void tc05_googleLogin_ReturnsGoogleErrorFlag_WhenAuthFails() {
        var u = User.builder().email("googleuser@somemail.com").build();
        String jwtToken = "mocked-INVALID-JWT-token";

        when(googleAuthService.verifyToken(jwtToken))
                .thenReturn(Optional.empty());

        Flag flag = service.googleLogin(jwtToken);

        assertEquals(UserFlag.GOOGLE_LOGIN_ERROR, flag);
    }

    @Test
    public void tc06_facebookLogin_ReturnsFacebookSuccessFlag_WhenSuccessful() {
        var u = User.builder().email("face@somemail.com").build();
        String jwtToken = "mocked-JWT-token";

        when(facebookAuthService.verifyToken(jwtToken))
                .thenReturn(Optional.of(u));
        when(repository.findByEmail(u.getEmail()))
                .thenReturn(Optional.of(u));

        Flag flag = service.facebookLogin(jwtToken);

        assertEquals(UserFlag.FACEBOOK_LOGIN_SUCCESS, flag);
    }

    @Test
    public void tc07_facebookLogin_ReturnsFacebookErrorFlag_WhenAuthFails() {
        var u = User.builder().email("face@somemail.com").build();
        String jwtToken = "mocked-INVALID-JWT-token";

        when(facebookAuthService.verifyToken(jwtToken))
                .thenReturn(Optional.empty());

        Flag flag = service.facebookLogin(jwtToken);

        assertEquals(UserFlag.FACEBOOK_LOGIN_ERROR, flag);
    }

    @Test
    public void tc08_changePassword_ReturnsChangePwdSuccessFlag_WhenSuccessful()
    {
        var u = User.builder().email("usuario@somemail.com").build();
        var newPwd = "000000";
        var newPwdConfirm = "000000";
        String appToken = "505050";

        when(appTokenService.verifyToken(appToken))
                .thenReturn(PwdUserFlag.VALID_TOKEN);
        when(repository.findByEmail(u.getEmail()))
                .thenReturn(Optional.of(u));

        Flag flag = service.changePassword(u, appToken, newPwd, newPwdConfirm);

        assertEquals(UserFlag.CHANGE_PASSWORD_SUCCESS, flag);
    }

    @Test
    public void tc09_changePassword_ReturnsChangePwdErrorFlag_WhenTokenIsInvalid()
    {
        var u = User.builder().email("usuario@somemail.com").build();
        var newPwd = "000000";
        var newPwdConfirm = "000000";
        String appToken = "515151";

        when(appTokenService.verifyToken(appToken))
                .thenReturn(PwdUserFlag.INVALID_TOKEN);

        Flag flag = service.changePassword(u, appToken, newPwd, newPwdConfirm);

        assertEquals(UserFlag.CHANGE_PASSWORD_ERROR, flag);
    }

    @Test
    public void tc10_changePassword_ReturnsChangePwdErrorFlag_WhenNewPwdConfirmIsInvalid()
    {
        var u = User.builder().email("usuario@somemail.com").build();
        var newPwd = "000000";
        var newPwdConfirm = "000001";
        String appToken = "505050";

        when(appTokenService.verifyToken(appToken))
                .thenReturn(PwdUserFlag.VALID_TOKEN);
        when(repository.findByEmail(u.getEmail()))
                .thenReturn(Optional.of(u));

        Flag flag = service.changePassword(u, appToken, newPwd, newPwdConfirm);

        assertEquals(UserFlag.CHANGE_PASSWORD_ERROR, flag);
    }
}
