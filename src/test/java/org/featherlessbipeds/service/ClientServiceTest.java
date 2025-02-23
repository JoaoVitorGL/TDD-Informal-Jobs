package org.featherlessbipeds.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest
{
    // Actual string is too long.
    private final String photo = "mocked-base64-encoded-photo";
    private Client originalClientData;

    @Mock
    private ClientRepository repository;
    @InjectMocks
    private ClientService service;

    @BeforeEach
    public void setUp()
    {
        originalClientData = Client.builder()
                .id(1)
                .email("usuario@somemail.com")
                .name("Patrick")
                .surname("MyGuy")
                .cep("11111-111")
                .photo(photo)
                .build();
    }

    @Test
    public void tc01_editProfile_ReturnsSuccessFlag_WhenSuccessful()
    {
        Client newClientData = Client.builder()
                .email("newEmail@gmail.com")
                .name("Helmuth")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        // Verify if user exists
        when(repository.findById(originalClientData.getId()))
                .thenReturn(Optional.of(originalClientData));
        // Verify if email is available or if its from the same user
        when(repository.findByEmail(newClientData.getEmail()))
                .thenReturn(Optional.of(originalClientData));
        // Updates user info
        when(repository.update(newClientData))
                .thenReturn(true);

        ClientFlag flag = service.editProfile(originalClientData, newClientData);

        asserEquals(ClientFlag.EDIT_PROFILE_SUCCESS, flag);

        verify(repository, times(1)).findById(originalClientData.getId());
        verify(repository, times(1)).findByEmail(newClientData.getEmail());
        verify(repository, times(1)).update(newClientData);
    }

    @Test
    public void tc02_editProfile_ReturnsErrorFlag_WhenEmailAlreadyExists()
    {
        Client newClientData = Client.builder()
                .email("alreadyExistingEmail.com")
                .name("Helmuth")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        Client otherClient = Client.builder().id(2).email("alreadyExistingEmail.com").build();

        when(repository.findById(originalClientData.getId()))
                .thenReturn(Optional.of(originalClientData));
        when(repository.findByEmail(newClientData.getEmail()))
                .thenReturn(Optional.of(otherClient));

        ClientFlag flag = service.editProfile(originalClientData, newClientData);

        asserEquals(ClientFlag.EDIT_PROFILE_ERROR, flag);

        verify(repository, times(1)).findById(originalClientData.getId());
        verify(repository, times(1)).findByEmail(newClientData.getEmail());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc03_editProfile_ReturnsErrorFlag_WhenNameIsEmpty()
    {
        Client newClientData = Client.builder()
                .email("newEmail@gmail.com")
                .name("")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        ClientFlag flag = service.editProfile(originalClientData, newClientData);

        asserEquals(ClientFlag.EDIT_PROFILE_ERROR, flag);

        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc04_editProfile_ReturnsErrorFlag_WhenSurnameIsEmpty()
    {
        Client newClientData = Client.builder()
                .email("newEmail@gmail.com")
                .name("Helmuth")
                .surname("")
                .cep("00000-000")
                .photo(photo)
                .build();

        ClientFlag flag = service.editProfile(originalClientData, newClientData);

        asserEquals(ClientFlag.EDIT_PROFILE_ERROR, flag);

        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc05_editProfile_ReturnsErrorFlag_WhenEmailIsEmpty()
    {
        Client newClientData = Client.builder()
                .email("")
                .name("Helmuth")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        ClientFlag flag = service.editProfile(originalClientData, newClientData);

        asserEquals(ClientFlag.EDIT_PROFILE_ERROR, flag);

        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc06_editProfile_ReturnsErrorFlag_WhenCepIsEmpty()
    {
        Client newClientData = Client.builder()
                .email("newEmail@gmail.com")
                .name("Helmuth")
                .surname("Voss")
                .cep("")
                .photo(photo)
                .build();

        ClientFlag flag = service.editProfile(originalClientData, newClientData);

        asserEquals(ClientFlag.EDIT_PROFILE_ERROR, flag);

        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc07_editProfile_ReturnsErrorFlag_WhenEmailIsInvalid()
    {
        Client newClientData = Client.builder()
                .email("32132143254643")
                .name("Helmuth")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        ClientFlag flag = service.editProfile(originalClientData, newClientData);

        asserEquals(ClientFlag.EDIT_PROFILE_ERROR, flag);

        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }
}
