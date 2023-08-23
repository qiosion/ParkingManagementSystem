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

    /*
    메서드명 : ordering
    매개변수
        - Long parkNumber: 주차번호
        - Model model
    메서드 설명 :
        주차 번호로 자동차 정보를 불러오고, 결제를 시도한다.
        결제를 성공했을 경우 출차를 시도한다. 성공하면 index 페이지로 이동
        결제 혹은 출차를 실패했을 경우 checkout.jsp 페이지로 이동한다

    */
    @PostMapping("/process")
    public String ordering(@RequestParam("parkNumber") Long parkNumber,
                           Model model) {
        // 주차 관련 정보
        CarDTO carDTO = carService.findByPN(parkNumber);

        // 금액 계산 및 결제 시도
        int success = orderService.processOrder(parkNumber);

        if (success > 0) { // 결제 성공
            String successMsg = "주문 성공";
            model.addAttribute("msg", successMsg);

            // 출차 처리
            int exitResult = carService.exitCar(carDTO);
            if (exitResult > 0) { // 출차 성공
                return "index";
            } else { // 출차 실패
                String msg = "오류가 발생했습니다.";
                model.addAttribute("msg", msg);
                return "chekcout";
            }
        } else {
            // 결제 실패
            String failMsg = "주문 실패";
            model.addAttribute("msg", failMsg);
            return "checkout";
        }
    }
}
