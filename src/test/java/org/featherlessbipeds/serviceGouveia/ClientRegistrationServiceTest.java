package org.featherlessbipeds.serviceGouveia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientRegistrationServiceTest
{
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void tc01_registerClient_ValidInformation() {
        Client client = new Client("John", "Smith", "john.smith@example.com", "Password123");
        when(clientRepository.emailExists("john.smith@example.com")).thenReturn(false);

        Client registered = clientService.registerClient(client, "Password123", "123456");

        assertNotNull(registered);
        assertEquals("John", registered.getFirstName());
        assertEquals("Smith", registered.getLastName());
        assertEquals("john.smith@example.com", registered.getEmail());
        assertEquals("Password123", registered.getPassword());

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void tc02_registerClient_InvalidConfirmationCode() {
        Client client = new Client("John", "Smith", "john.smith@example.com", "Password123");
        when(clientRepository.emailExists("john.smith@example.com")).thenReturn(false);

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "654321");
        });

        assertEquals("Invalid confirmation code.", exception.getMessage());
    }

    @Test
    void tc03_registerClient_ExistingEmail() {
        Client client = new Client("John", "Smith", "john.smith@example.com", "Password123");
        when(clientRepository.emailExists("john.smith@example.com")).thenReturn(true);

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "123456");
        });

        assertEquals("Email already registered.", exception.getMessage());
    }

    @Test
    void tc04_registerClient_InvalidFirstName() {
        Client client = new Client("John123", "Smith", "john.smith@example.com", "Password123");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "123456");
        });

        assertEquals("Invalid first name. It must only contain letters and spaces.", exception.getMessage());
    }

    @Test
    void tc05_registerClient_InvalidLastName() {
        Client client = new Client("John", "Smith123", "john.smith@example.com", "Password123");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "123456");
        });

        assertEquals("Invalid last name. It must only contain letters and spaces.", exception.getMessage());
    }

    @Test
    void tc06_registerClient_InvalidEmail() {
        Client client = new Client("John", "Smith", "john.smith", "Password123");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "123456");
        });

        assertEquals("Invalid email.", exception.getMessage());
    }

    @Test
    void tc07_registerClient_InvalidPassword() {
        Client client = new Client("John", "Smith", "john.smith@example.com", "password");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "password", "123456");
        });

        assertEquals("Invalid password. It must have at least 8 characters, an uppercase letter, and a number.", exception.getMessage());
    }

    @Test
    void tc08_registerClient_InvalidPasswordConfirmation() {
        Client client = new Client("John", "Smith", "john.smith@example.com", "Password123");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password1234", "123456");
        });

        assertEquals("Password confirmation does not match.", exception.getMessage());
    }

    @Test
    void tc09_registerClient_BlankFirstName() {
        Client client = new Client("", "Smith", "john.smith@example.com", "Password123");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "123456");
        });

        assertEquals("The first name field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc10_registerClient_BlankLastName() {
        Client client = new Client("John", "", "john.smith@example.com", "Password123");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "123456");
        });

        assertEquals("The last name field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc11_registerClient_BlankEmail() {
        Client client = new Client("John", "Smith", "", "Password123");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "123456");
        });

        assertEquals("The last name field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc12_registerClient_BlankPassword() {
        Client client = new Client("John", "Smith", "john.smith@example.com", "");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "Password123", "123456");
        });

        assertEquals("The password field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc13_registerClient_BlankPassword() {
        Client client = new Client("John", "Smith", "john.smith@example.com", "Password123");

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            clientService.registerClient(client, "", "123456");
        });

        assertEquals("The confirm password field cannot be blank.", exception.getMessage());
    }
}
