package org.featherlessbipeds.service;

import org.featherlessbipeds.exception.EditProfileException;
import org.featherlessbipeds.model.Freelancer;
import org.featherlessbipeds.repository.contracts.FreelancerRepository;
import org.featherlessbipeds.utils.EditProfileFlag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FreelancerServiceTest {

    private final String photo = "photo-do-cabra";
    private Freelancer defaultFreelancer;

    @Mock
    private FreelancerRepository repository;
    @InjectMocks
    private  FreelancerService service;

    @BeforeEach
    public void setUp()
    {
        defaultFreelancer = Freelancer.builder()
                .id(1)
                .name("Ze")
                .email("ze@avanade.com")
                .surname("Do Rodo")
                .cep("50000-000")
                .photo(photo)
                .build();
    }




    @Test
    public void tc01_editProfile_ReturnsFreelancer_WhenSuccessful() throws EditProfileException
    {
        Freelancer newFreelancerData = Freelancer.builder()
                .name("Chico")
                .email("chico@accenture.com")
                .surname("Da pa")
                .cep("60000-000")
                .photo(photo)
                .build();

        when(repository.findById(defaultFreelancer.getId())).thenReturn(Optional.of(this.defaultFreelancer));
        when(repository.findByEmail(newFreelancerData.getEmail())).thenReturn(Optional.empty());
        when(repository.update(defaultFreelancer)).thenReturn(Optional.of(this.defaultFreelancer));

        Freelancer result = service.updateFreelancer(defaultFreelancer,newFreelancerData);

        assertNotNull(result);

        assertNotNull(defaultFreelancer);
        assertNotNull(result);
        assertEquals(defaultFreelancer.getName(), result.getName());
        assertEquals(defaultFreelancer.getPhoto(), result.getPhoto());
        assertEquals(defaultFreelancer.getSurname(), result.getSurname());
        assertEquals(defaultFreelancer.getEmail(), result.getEmail());
        assertEquals(defaultFreelancer.getCep(), result.getCep());

        verify(repository, times(1)).findById(this.defaultFreelancer.getId());
        verify(repository, times(1)).findByEmail(newFreelancerData.getEmail());
        verify(repository, times(1)).update(this.defaultFreelancer);


    }

    @Test
    public void tc02_editProfile_ThrowsException_WhenEmailAlreadyExists()
    {
        Freelancer newFreelancerData = Freelancer.builder()
                .name("Fabo")
                .email("luiz@accenture.com")
                .surname("Da foice")
                .cep("70000-000")
                .photo(photo)
                .build();

        when(repository.findById(defaultFreelancer.getId())).thenReturn(Optional.of(this.defaultFreelancer));
        when(repository.findByEmail(newFreelancerData.getEmail())).thenReturn(
                Optional.of(Freelancer.builder()
                         .id(2)
                        .name("luiz da accenture")
                        .email("luiz@accenture.com")
                        .surname("Da accenture")
                        .cep("60000-000")
                        .photo(photo)
                        .build()));


        EditProfileException exception = assertThrows(EditProfileException.class,
                () -> service.updateFreelancer(defaultFreelancer,newFreelancerData));

        assertEquals(exception.getFlag(), EditProfileFlag.EDIT_PROFILE_EMAIL_CONFLICT);

        verify(repository, times(1)).findById(this.defaultFreelancer.getId());
        verify(repository, times(1)).findByEmail(newFreelancerData.getEmail());
        verify(repository, times(0)).update(any());

    }

    @Test
    public void tc03_editProfile_ThrowsException_WhenNameIsEmpty()
    {
        Freelancer newFreelancerData = Freelancer.builder()
                .name("")
                .email("chico@accenture.com")
                .surname("Da picareta")
                .cep("80000-000")
                .photo(photo)
                .build();

       EditProfileException exception = assertThrows(EditProfileException.class,
               () ->  service.updateFreelancer(defaultFreelancer,newFreelancerData)
               );

       assertEquals(exception.getFlag(), EditProfileFlag.EDIT_PROFILE_EMPTY_NAME);

        verify(repository, times(0)).findById(any());
        verify(repository, times(0)).findByEmail(any());
        verify(repository, times(0)).update(any());

    }

    @Test
    public void tc04_editProfile_ThrowsException_WhenSurnameIsEmpty()
    {
        Freelancer newFreelancerData = Freelancer.builder()
                .name("Flavio")
                .email("flavio@accenture.com")
                .surname("")
                .cep("90000-000")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () ->  service.updateFreelancer(defaultFreelancer,newFreelancerData)
        );

        assertEquals(exception.getFlag(), EditProfileFlag.EDIT_PROFILE_EMPTY_SURNAME);

        verify(repository, times(0)).findById(any());
        verify(repository, times(0)).findByEmail(any());
        verify(repository, times(0)).update(any());
    }

    @Test
    public void tc05_editProfile_ThrowsException_WhenEmailIsEmpty()
    {
        Freelancer newFreelancerData = Freelancer.builder()
                .name("Breno")
                .email("")
                .surname("Do martelo")
                .cep("11000-000")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () ->  service.updateFreelancer(defaultFreelancer,newFreelancerData)
        );

        assertEquals(exception.getFlag(), EditProfileFlag.EDIT_PROFILE_EMPTY_EMAIL);

        verify(repository, times(0)).findById(any());
        verify(repository, times(0)).findByEmail(any());
        verify(repository, times(0)).update(any());
    }

    @Test
    public void tc06_editProfile_ThrowsException_WhenCepIsEmpty()
    {
        Freelancer newFreelancerData = Freelancer.builder()
                .name("Jerson")
                .email("jerson@pitang.com")
                .surname("Do trator")
                .cep("")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () ->  service.updateFreelancer(defaultFreelancer,newFreelancerData)
        );

        assertEquals(exception.getFlag(), EditProfileFlag.EDIT_PROFILE_EMPTY_CEP);

        verify(repository, times(0)).findById(any());
        verify(repository, times(0)).findByEmail(any());
        verify(repository, times(0)).update(any());
    }

    @Test
    public void tc07_editProfile_ThrowsException_WhenEmailIsInvalid()
    {
        Freelancer newFreelancerData = Freelancer.builder()
                .name("Xand")
                .email("xand_pitang.com")
                .surname("Do aviao")
                .cep("12000-000")
                .photo(photo)
                .build();

        EditProfileException exception = assertThrows(EditProfileException.class,
                () ->  service.updateFreelancer(defaultFreelancer,newFreelancerData)
        );

        assertEquals(exception.getFlag(), EditProfileFlag.EDIT_PROFILE_INVALID_EMAIL);

        verify(repository, times(0)).findById(any());
        verify(repository, times(0)).findByEmail(any());
        verify(repository, times(0)).update(any());
    }

}
