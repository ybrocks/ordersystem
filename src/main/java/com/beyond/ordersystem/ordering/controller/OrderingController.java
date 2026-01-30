package com.beyond.ordersystem.ordering.controller;

import com.beyond.ordersystem.ordering.service.OrderingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordering")
public class OrderingController {
    private final OrderingService orderingService;

    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> create() {
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body();
    }

    @GetMapping("/myorders")
    public ResponseEntity<?> myOrders(){
        return ResponseEntity.status(HttpStatus.OK).body();
    }
}
