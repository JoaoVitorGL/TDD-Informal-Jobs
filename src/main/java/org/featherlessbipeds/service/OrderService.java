package org.featherlessbipeds.service;

import org.featherlessbipeds.exception.OrderServiceException;
import org.featherlessbipeds.model.Order;
import org.featherlessbipeds.repository.contracts.OrderRepository;
import org.featherlessbipeds.service.contracts.LocationService;
import org.featherlessbipeds.utils.OrderServiceFlag;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrderService {

    private OrderRepository repository;
    private LocationService locationService;
    private List<String> validFormats;

    public OrderService() {
        validFormats = Arrays.asList("jpg", "png", "pdf");
    }
    public Order orderService(Order order){

        if(order.getBudget() < 0)
            throw new OrderServiceException(OrderServiceFlag.ORDER_BUDGET_INVALID);

        if(order.getTermInDays() <0)
            throw new OrderServiceException(OrderServiceFlag.ORDER_TERM_INVALID);

        if(order.getDescription().isEmpty())
            throw new OrderServiceException(OrderServiceFlag.ORDER_DESCRIPTION_EMPTY);

        if(Objects.isNull(order.getServiceCategory()))
            throw new OrderServiceException(OrderServiceFlag.ORDER_SERVICE_CATEGORY_EMPTY);

        if(!attachmentValidation(order.getAttachment()))
            throw new OrderServiceException(OrderServiceFlag.ORDER_ATTACHMENT_FORMAT_INVALID);

        if(!locationService.isValidLocation(order.getLocation()))
            throw new OrderServiceException(OrderServiceFlag.ORDER_LOCATION_INVALID);

        if(locationService.isLocationFarFromAddress(order.getLocation(),order.getClient().getCep()))
            throw new OrderServiceException(OrderServiceFlag.ORDER_LOCATION_MISMATCH);

        return repository.save(order);
    }

    public boolean attachmentValidation(String att){
        var format = att.split("\\.")[1];

        return validFormats.contains(format);
    }


}
