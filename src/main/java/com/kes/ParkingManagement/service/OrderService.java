package com.kes.ParkingManagement.service;

import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.dto.OrderDTO;
import com.kes.ParkingManagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CarService carService;

    public int processOrder(Long parkNumber) {

        // 주차 정보 가져오기
        CarDTO carDTO = carService.findByPN(parkNumber);
        Long parkingFee = carDTO.getParkingFee();
        System.out.println("parkNumber : " + parkNumber);
        System.out.println("요금 : " + parkingFee);

        // 주문 정보 생성 및 저장
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setParkNumber(parkNumber);
        orderDTO.setAmount(parkingFee);
        orderDTO.setOrder_time(LocalDateTime.now());

        int result = orderRepository.createOrder(orderDTO);
        System.out.println("result : " + result);

        if (result > 0) { // 결제 성공
            return result;
        } else {
            return -1; // 결제 실패
        }
    }
}
