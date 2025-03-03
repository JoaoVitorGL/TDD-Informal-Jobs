package org.featherlessbipeds.service;

import org.featherlessbipeds.exception.EditProfileException;
import org.featherlessbipeds.model.Client;
import org.featherlessbipeds.repository.contracts.ClientRepository;
import org.featherlessbipeds.utils.EditProfileFlag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest
{
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

    private void usualAssertRoutine(Client c1, Client c2)
    {
        assertNotNull(c1);
        assertNotNull(c2);
        assertEquals(c1.getName(), c2.getName());
        assertEquals(c1.getPassword(), c2.getPassword());
        assertEquals(c1.getPhoto(), c2.getPhoto());
        assertEquals(c1.getSurname(), c2.getSurname());
        assertEquals(c1.getEmail(), c2.getEmail());
    }

    @Test
    public void tc01_editProfile_ReturnsClient_WhenSuccessful() throws EditProfileException
    {
        Client newClientData = Client.builder()
                .email("newEmail@gmail.com")
                .name("Helmuth")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        when(repository.findById(originalClientData.getId()))
                .thenReturn(Optional.of(originalClientData));
        when(repository.findByEmail(newClientData.getEmail()))
                .thenReturn(Optional.of(originalClientData));
        when(repository.update(newClientData))
                .thenReturn(Optional.of(newClientData));

        Client result = service.editProfile(originalClientData, newClientData);
        usualAssertRoutine(newClientData, result);

        verify(repository, times(1)).findById(originalClientData.getId());
        verify(repository, times(1)).findByEmail(newClientData.getEmail());
        verify(repository, times(1)).update(newClientData);
    }

    @Test
    public void tc02_editProfile_ThrowsException_WhenEmailAlreadyExists()
    {
        Client newClientData = Client.builder()
                .email("alreadyExistingEmail@mail.com")
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

        EditProfileException exception = assertThrows(EditProfileException.class,
                () -> service.editProfile(originalClientData, newClientData));

        assertEquals(EditProfileFlag.EDIT_PROFILE_EMAIL_CONFLICT, exception.getFlag());
        verify(repository, times(1)).findById(originalClientData.getId());
        verify(repository, times(1)).findByEmail(newClientData.getEmail());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc03_editProfile_ThrowsException_WhenNameIsEmpty()
    {
        Client newClientData = Client.builder()
                .email("newEmail@gmail.com")
                .name("")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () -> service.editProfile(originalClientData, newClientData));

        assertEquals(EditProfileFlag.EDIT_PROFILE_EMPTY_NAME, exception.getFlag());
        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc04_editProfile_ThrowsException_WhenSurnameIsEmpty()
    {
        Client newClientData = Client.builder()
                .email("newEmail@gmail.com")
                .name("Helmuth")
                .surname("")
                .cep("00000-000")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () -> service.editProfile(originalClientData, newClientData));

        assertEquals(EditProfileFlag.EDIT_PROFILE_EMPTY_SURNAME, exception.getFlag());
        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc05_editProfile_ThrowsException_WhenEmailIsEmpty()
    {
        Client newClientData = Client.builder()
                .email("")
                .name("Helmuth")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () -> service.editProfile(originalClientData, newClientData));

        assertEquals(EditProfileFlag.EDIT_PROFILE_EMPTY_EMAIL, exception.getFlag());
        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc06_editProfile_ThrowsException_WhenCepIsEmpty()
    {
        Client newClientData = Client.builder()
                .email("newEmail@gmail.com")
                .name("Helmuth")
                .surname("Voss")
                .cep("")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () -> service.editProfile(originalClientData, newClientData));

        assertEquals(EditProfileFlag.EDIT_PROFILE_EMPTY_CEP, exception.getFlag());
        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }

    @Test
    public void tc07_editProfile_ThrowsException_WhenEmailIsInvalid()
    {
        Client newClientData = Client.builder()
                .email("32132143254643")
                .name("Helmuth")
                .surname("Voss")
                .cep("00000-000")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () -> service.editProfile(originalClientData, newClientData));

        assertEquals(EditProfileFlag.EDIT_PROFILE_INVALID_EMAIL, exception.getFlag());
        verify(repository, never()).findById(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).update(any());
    }
}