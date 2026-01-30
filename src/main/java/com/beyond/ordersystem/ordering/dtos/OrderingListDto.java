package com.beyond.ordersystem.ordering.dtos;

import com.beyond.ordersystem.ordering.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderingListDto {
    private Long id;
    private String memberEmail;
    private OrderStatus orderStatus;
    List<OrderDetailDto> orderDetails;
}
