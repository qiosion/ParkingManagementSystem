package com.kes.ParkingManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long orderNumber;
    private Long parkNumber ;
    private String amount;
    private LocalDateTime order_time ;
}
