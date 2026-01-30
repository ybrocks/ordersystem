package com.beyond.ordersystem.ordering.service;

import com.beyond.ordersystem.ordering.repository.OrderingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class OrderingService {
    private final OrderingRepository orderingRepository;

    public OrderingService(OrderingRepository orderingRepository) {
        this.orderingRepository = orderingRepository;
    }

    public Long create(){
        return ordering.getId();
    }


    @Transactional(readOnly = true)
    public List<> findAll(){
        return ;
    }


    @Transactional(readOnly = true)
    public List<> myorders(){
        return ;
    }

}
