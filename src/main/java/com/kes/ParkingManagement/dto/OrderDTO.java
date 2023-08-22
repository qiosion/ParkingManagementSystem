package com.kes.ParkingManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long orderNumber; // 주문번호
    private Long parkNumber; // 주차번호
    private Long amount; // 주문 금액
    private LocalDateTime order_time; // 결제시각
}
