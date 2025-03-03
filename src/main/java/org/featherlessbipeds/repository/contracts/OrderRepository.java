package org.featherlessbipeds.repository.contracts;

import org.featherlessbipeds.model.Order;

public interface OrderRepository {

    Order save(Order newOrder);

}
