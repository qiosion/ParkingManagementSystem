package com.kes.ParkingManagement.controller;

import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.service.CarService;
import com.kes.ParkingManagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CarService carService;

    @PostMapping("/process")
    public String ordering(@RequestParam("parkNumber") Long parkNumber,
                           Model model) {

        System.out.println("order 컨트롤러 주차번호 : " + parkNumber);
        // 주차 관련 정보
        CarDTO carDTO = carService.findByPN(parkNumber);
        System.out.println("컨트롤러 carDTO : " + carDTO);

        // 금액 계산 및 결제 시도
        int success = orderService.processOrder(parkNumber);

        System.out.println("컨트롤러 success : " + success);

        if (success > 0) { // 결제 성공
            String successMsg = "주문 성공";
            model.addAttribute("msg", successMsg);
        } else {
            // 결제 실패
            String failMsg = "주문 실패";
            model.addAttribute("msg", failMsg);
        }

        return "index";
    }
}
