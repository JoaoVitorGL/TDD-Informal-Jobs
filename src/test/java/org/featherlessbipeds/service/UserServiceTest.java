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


}
