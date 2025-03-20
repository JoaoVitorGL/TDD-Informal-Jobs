package org.featherlessbipeds.serviceGouveia;

import org.featherlessbipeds.gouveia.exception.RegisterException;
import org.featherlessbipeds.gouveia.model.Freelancer;
import org.featherlessbipeds.gouveia.model.FreelancerTagsEnum;
import org.featherlessbipeds.gouveia.repository.FreelancerRepository;
import org.featherlessbipeds.gouveia.service.FreelancerRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FreelancerRegisterServiceTest {
    @Mock
    private FreelancerRepository freelancerRepository;
    @InjectMocks
    private FreelancerRegisterService freelancerService;

    private List<FreelancerTagsEnum> tags;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tags = new ArrayList<>(List.of(FreelancerTagsEnum.WEB_DEVELOPER, FreelancerTagsEnum.GRAPHIC_DESIGNER));
    }

    @Test
    void tc01_registerFreelancer_ValidInformation() throws RegisterException {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com", "Password123", "12345678900", tags);
        when(freelancerRepository.existsByEmail("john.smith@example.com")).thenReturn(false);

        Freelancer registered = freelancerService.registerFreelancer(freelancer, "Password123", "123456");

        assertNotNull(registered);
        assertEquals("John", registered.getName());
        assertEquals("Smith", registered.getSurname());
        assertEquals("john.smith@example.com", registered.getEmail());
        assertEquals("Password123", registered.getPassword());
        assertEquals("12345678900", registered.getCpf());
        assertEquals(tags, registered.getTags());

        verify(freelancerRepository, times(1)).save(freelancer);
    }

    @Test
    void tc02_registerFreelancer_ExistingEmail() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com", "Password123",
                "12345678900", tags);
        when(freelancerRepository.existsByEmail("john.smith@example.com")).thenReturn(true);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("Email already registered.", exception.getMessage());
    }

    @Test
    void tc03_registerFreelancer_ExistingCpf() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com", "Password123",
                "12345678900", tags);
        when(freelancerRepository.existsByCpf("12345678900")).thenReturn(true);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("CPF already registered.", exception.getMessage());
    }

    @Test
    void tc04_registerFreelancer_InvalidConfirmationCode() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com", "Password123",
                "12345678900", tags);
        when(freelancerRepository.existsByEmail("john.smith@example.com")).thenReturn(false);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "654321");
        });

        assertEquals("Invalid confirmation code.", exception.getMessage());
    }

    @Test
    void tc05_registerFreelancer_InvalidName() {
        Freelancer freelancer = new Freelancer("John123", "Smith", "john.smith@example.com", "Password123",
                "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("Invalid name. It must only contain letters and spaces.", exception.getMessage());
    }

    @Test
    void tc06_registerFreelancer_InvalidSurname() {
        Freelancer freelancer = new Freelancer("John", "Smith123", "john.smith@example.com",
                "Password123", "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("Invalid surname. It must only contain letters and spaces.", exception.getMessage());
    }

    @Test
    void tc07_registerFreelancer_InvalidEmail() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith", "Password123",
                "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("Invalid e-mail.", exception.getMessage());
    }

    @Test
    void tc08_registerFreelancer_InvalidCpf() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com",
                "Password123", "abc123", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("Invalid CPF.", exception.getMessage());
    }

    @Test
    void tc09_registerFreelancer_InvalidPassword() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com",
                "password", "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "password", "123456");
        });

        assertEquals("Invalid password. It must have at least 8 characters, an uppercase letter, and a number.", exception.getMessage());
    }

    @Test
    void tc10_registerFreelancer_InvalidPasswordConfirmation() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com",
                "Password123", "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password1234", "123456");
        });

        assertEquals("Password confirmation does not match.", exception.getMessage());
    }

    @Test
    void tc11_registerFreelancer_BlankName() {
        Freelancer freelancer = new Freelancer("", "Smith", "john.smith@example.com",
                "Password123", "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("The name field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc12_registerFreelancer_BlankSurnameName() {
        Freelancer freelancer = new Freelancer("John", "", "john.smith@example.com",
                "Password123", "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("The surname field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc13_registerFreelancer_BlankEmail() {
        Freelancer freelancer = new Freelancer("John", "Smith", "",
                "Password123", "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("The e-mail field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc14_registerFreelancer_BlankCPF() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com",
                "Password123", "", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("The CPF field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc15_registerFreelancer_BlankPassword() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com",
                "", "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("The password field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc16_registerFreelancer_BlankConfirmPassword() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com",
                "Password123", "12345678900", tags);

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "", "123456");
        });

        assertEquals("The confirm password field cannot be blank.", exception.getMessage());
    }

    @Test
    void tc17_registerFreelancer_BlankTags() {
        Freelancer freelancer = new Freelancer("John", "Smith", "john.smith@example.com",
                "Password123", "12345678900", List.of());

        RegisterException exception = assertThrows(RegisterException.class, () -> {
            freelancerService.registerFreelancer(freelancer, "Password123", "123456");
        });

        assertEquals("Select at least one tag.", exception.getMessage());
    }
}
