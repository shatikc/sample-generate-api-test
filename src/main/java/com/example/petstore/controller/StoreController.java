package com.example.petstore.controller;

import com.example.petstore.api.StoreApi;
import com.example.petstore.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class StoreController implements StoreApi {


    @Override
    public ResponseEntity<Void> deleteOrder(Long orderId) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Map<String, Integer>>> getInventory() {
    return null;
    }

    @Override
    public ResponseEntity<Order> getOrderById(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setPetId(10L);
        order.complete(false);
        order.setQuantity(1);
        order.setStatus(Order.StatusEnum.APPROVED);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Order> placeOrder(@Valid Order order) {
        return null;
    }
}