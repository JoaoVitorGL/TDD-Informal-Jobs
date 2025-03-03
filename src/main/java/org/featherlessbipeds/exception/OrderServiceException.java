package org.featherlessbipeds.exception;

import lombok.Getter;
import org.featherlessbipeds.utils.OrderServiceFlag;

@Getter
public class OrderServiceException extends RuntimeException {

    private final OrderServiceFlag flag;

    public OrderServiceException(OrderServiceFlag flag)
    {
        super("Order failed: "+flag);
        this.flag = flag;
    }
}
