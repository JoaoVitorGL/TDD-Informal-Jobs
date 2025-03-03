package org.featherlessbipeds.service;

import org.featherlessbipeds.Enums.ServicesCategoriesEnum;
import org.featherlessbipeds.exception.OrderServiceException;
import org.featherlessbipeds.model.Client;
import org.featherlessbipeds.model.LocationEntity;
import org.featherlessbipeds.model.Order;
import org.featherlessbipeds.repository.contracts.OrderRepository;
import org.featherlessbipeds.service.contracts.LocationService;
import org.featherlessbipeds.utils.OrderServiceFlag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceServiceTest {

    private final String attachment = "anexo.pdf";
    private final String photo = "retrato_do_cabra";

    @Mock
    private OrderRepository repository;
    @Mock
    private LocationService locationService;
    @Spy
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

   @Test
    public void tc01_client_orders_service_success(){

        Order newOrder = Order.builder()
                .id(1)
                .client(defaultClient)
                .serviceCategory(ServicesCategoriesEnum.TECNOLOGIA)
                .termInDays(14)
                .description("App android para prever saude de ar condicionados")
                .budget(700.00)
                .attachment(attachment)
                .location(new LocationEntity(-35.7895,-8.7458))
                .build();

        when(repository.save(newOrder)).thenReturn(newOrder);
        when(locationService.isValidLocation(newOrder.getLocation())).thenReturn(true);
        when(locationService.isLocationFarFromAddress(newOrder.getLocation(),defaultClient.getCep())).thenReturn(false);

        var createdOrder = orderServiceService.orderService(newOrder);

        assertNotNull(createdOrder);

        verify(repository,times(1)).save(newOrder);
        verify(orderServiceService,times(1)).attachmentValidation(newOrder.getAttachment());

    }
    @Test
    public void tc02_client_orders_service_invalid_budget(){
        Order newOrder = Order.builder()
                .id(1)
                .client(defaultClient)
                .serviceCategory(ServicesCategoriesEnum.TECNOLOGIA)
                .termInDays(14)
                .description("App android para prever saude de ar condicionados")
                //Valor negativo
                .budget(-700.00)
                .attachment(attachment)
                .location(new LocationEntity(-35.7895,-8.7458))
                .build();

       OrderServiceException exception =  assertThrows(OrderServiceException.class,
               () -> orderServiceService.orderService(newOrder)
       );

       assertEquals(exception.getFlag(), OrderServiceFlag.ORDER_BUDGET_INVALID);

        verify(repository,never()).save(newOrder);
        verify(orderServiceService,never()).attachmentValidation(any());
        verify(locationService,never()).isValidLocation(any());
        verify(locationService,never()).isLocationFarFromAddress(any(),any());
    }
    @Test
    public void tc03_client_orders_service_invalid_term(){
        Order newOrder = Order.builder()
                .id(1)
                .client(defaultClient)
                .serviceCategory(ServicesCategoriesEnum.TECNOLOGIA)
                //Valor negativo
                .termInDays(-14)
                .description("App android para prever saude de ar condicionados")
                .budget(700.00)
                .attachment(attachment)
                .location(new LocationEntity(-35.7895,-8.7458))
                .build();

        OrderServiceException exception =  assertThrows(OrderServiceException.class,
                () -> orderServiceService.orderService(newOrder)
        );

        assertEquals(exception.getFlag(), OrderServiceFlag.ORDER_TERM_INVALID);

        verify(repository,never()).save(newOrder);
        verify(orderServiceService,never()).attachmentValidation(any());
        verify(locationService,never()).isValidLocation(any());
        verify(locationService,never()).isLocationFarFromAddress(any(),any());
    }
    @Test
    public void tc04_client_orders_service_invalid_attachment(){
        Order newOrder = Order.builder()
                .id(1)
                .client(defaultClient)
                .serviceCategory(ServicesCategoriesEnum.TECNOLOGIA)
                .termInDays(14)
                .description("App android para prever saude de ar condicionados")
                .budget(700.00)
                //Formato de anexo invalido
                .attachment("anexo.exe")
                .location(new LocationEntity(-35.7895,-8.7458))
                .build();

        when(orderServiceService.attachmentValidation(newOrder.getAttachment())).thenReturn(false);

        OrderServiceException exception =  assertThrows(OrderServiceException.class,
                () -> orderServiceService.orderService(newOrder)
        );

        assertEquals(exception.getFlag(), OrderServiceFlag.ORDER_ATTACHMENT_FORMAT_INVALID);

        verify(repository,never()).save(newOrder);
        verify(locationService,never()).isValidLocation(any());
        verify(locationService,never()).isLocationFarFromAddress(any(),any());
    }
    @Test
    public void tc05_client_orders_service_empty_description(){

        Order newOrder = Order.builder()
                .id(1)
                .client(defaultClient)
                .serviceCategory(ServicesCategoriesEnum.TECNOLOGIA)
                .termInDays(14)
                .description("")
                .budget(700.00)
                .attachment(attachment)
                .location(new LocationEntity(-35.7895,-8.7458))
                .build();

        OrderServiceException exception =  assertThrows(OrderServiceException.class,
                () -> orderServiceService.orderService(newOrder)
        );

        assertEquals(exception.getFlag(), OrderServiceFlag.ORDER_DESCRIPTION_EMPTY);

        verify(repository,never()).save(newOrder);
        verify(orderServiceService,never()).attachmentValidation(any());
        verify(locationService,never()).isValidLocation(any());
        verify(locationService,never()).isLocationFarFromAddress(any(),any());

    }
    @Test
    public void tc06_client_orders_service_null_service_category(){

        Order newOrder = Order.builder()
                .id(1)
                .client(defaultClient)
                .serviceCategory(null)
                .termInDays(14)
                .description("App android para prever saude de ar condicionados")
                .budget(700.00)
                .attachment(attachment)
                .location(new LocationEntity(-35.7895,-8.7458))
                .build();

        OrderServiceException exception =  assertThrows(OrderServiceException.class,
                () -> orderServiceService.orderService(newOrder)
        );

        assertEquals(exception.getFlag(), OrderServiceFlag.ORDER_SERVICE_CATEGORY_EMPTY);

        verify(repository,never()).save(newOrder);
        verify(orderServiceService,never()).attachmentValidation(any());
        verify(locationService,never()).isValidLocation(any());
        verify(locationService,never()).isLocationFarFromAddress(any(),any());


    }
    @Test
    public void tc07_client_orders_service_invalid_location(){
        Order newOrder = Order.builder()
                .id(1)
                .client(defaultClient)
                .serviceCategory(ServicesCategoriesEnum.TECNOLOGIA)
                .termInDays(14)
                .description("App android para prever saude de ar condicionados")
                .budget(700.00)
                .attachment(attachment)
                .location(new LocationEntity(-10.7895,-35.7458))
                .build();

        when(locationService.isValidLocation(newOrder.getLocation())).thenReturn(false);

        OrderServiceException exception =  assertThrows(OrderServiceException.class,
                () -> orderServiceService.orderService(newOrder)
        );

        assertEquals(exception.getFlag(), OrderServiceFlag.ORDER_LOCATION_INVALID);

        verify(repository,never()).save(newOrder);

    }
    @Test
    public void tc08_client_orders_service_location_too_far_from_CEP(){

        Order newOrder = Order.builder()
                .id(1)
                .client(defaultClient)
                .serviceCategory(ServicesCategoriesEnum.TECNOLOGIA)
                .termInDays(14)
                .description("App android para prever saude de ar condicionados")
                .budget(700.00)
                .attachment(attachment)
                .location(new LocationEntity(-35.7895,-8.7458))
                .build();

        when(locationService.isValidLocation(newOrder.getLocation())).thenReturn(true);
        when(locationService.isLocationFarFromAddress(newOrder.getLocation(),newOrder.getClient().getCep())).thenReturn(true);

        OrderServiceException exception =  assertThrows(OrderServiceException.class,
                () -> orderServiceService.orderService(newOrder)
        );

        assertEquals(exception.getFlag(), OrderServiceFlag.ORDER_LOCATION_MISMATCH);

        verify(repository,never()).save(newOrder);


    }

}
