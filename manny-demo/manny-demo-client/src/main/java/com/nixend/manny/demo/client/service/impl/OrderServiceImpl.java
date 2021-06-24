package com.nixend.manny.demo.client.service.impl;

import com.nixend.manny.core.annotation.PostRoute;
import com.nixend.manny.core.annotation.RequestRoute;
import com.nixend.manny.demo.client.model.Order;
import com.nixend.manny.demo.client.service.OrderService;

/**
 * @author panyox
 */
@RequestRoute("order")
public class OrderServiceImpl implements OrderService {

    @Override
    @PostRoute("create")
    public Order createOrder(Order order) {
        return order;
    }
}
