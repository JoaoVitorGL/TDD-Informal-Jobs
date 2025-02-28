package org.featherlessbipeds.service;

import org.featherlessbipeds.model.Client;
import org.featherlessbipeds.repository.contracts.OrderRepository;
import org.featherlessbipeds.service.contracts.LocationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceServiceTest {

    private final String photo = "foto_do_cabra.png";

    @Mock
    private OrderRepository repository;
    @Mock
    private LocationService locationService;
    @InjectMocks
    private OrderServiceService orderServiceService;

    private Client defaultClient;

    @BeforeEach
    public  void setUp(){
        defaultClient = Client.builder()
                .id(1)
                .email("batman@jmail.com")
                .name("Michael")
                .surname("Do Gas")
                .cep("00000-000")
                .photo(photo)
                .build();
    }


    public void tc01_client_orders_service_success(){



    }

    public void tc02_editProfile_ReturnsFreelancer_WhenSuccessful(){

    }

    public void tc03_editProfile_ReturnsFreelancer_WhenSuccessful(){

    }

    public void tc04_editProfile_ReturnsFreelancer_WhenSuccessful(){

    }

    public void tc05_editProfile_ReturnsFreelancer_WhenSuccessful(){

    }

    public void tc06_editProfile_ReturnsFreelancer_WhenSuccessful(){

    }

    public void tc07_editProfile_ReturnsFreelancer_WhenSuccessful(){

    }

    public void tc08_editProfile_ReturnsFreelancer_WhenSuccessful(){

    }

}
