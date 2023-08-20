package com.kes.ParkingManagement.controller;

import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    /*
    메서드명 : in
    매개변수
        - CarDTO : 자동차 정보
        - model
    메서드 설명 : 입차. input으로 넣은 차번호와 entryTime을 삽입
        - 주차번호는 자동증가
        - state는 '입차'가 디폴트값
    */
    @PostMapping("/park")
    public String in(@ModelAttribute CarDTO carDTO, Model model){
        String carNumber = carDTO.getCarNumber(); // 차번호

        int parkResult = carService.parkCar(carDTO);
        if (parkResult > 0) { // 주차 성공
            return "redirect:/car/list";
        } else if (parkResult == -1) { // 중복 차량
            String msg = "중복된 차번호로 주차할 수 없습니다";
            model.addAttribute("msg", msg);
            return "index";
        } else { // 주차 실패
            return "index"; // index.jsp로 이동
        }
    }

    /*
    메서드명 : list
    매개변수
        - CarDTO : 자동차 정보
        - model
    메서드 설명 : 현재 주차중인 차량 정보를 조회
    carDTOList :
        입차/출차 된 차의 내역을 출력.
        parkingLot.jsp 에서 carList 를 통해 사용가능.
    */
    @GetMapping("/list")
    public String carList(@ModelAttribute CarDTO carDTO, Model model){
        List<CarDTO> carDTOList = carService.viewNow();
        model.addAttribute("carList", carDTOList);
        return "parkingLot"; // parkingLot.jsp로 이동
    }

    /*
    메서드명 : out
    매개변수
        - CarDTO : 자동차 정보
        - model
    메서드 설명 : 출차. exitTime에 현재시각을 등록하고 state 를 '출차'로 변경
    */
    @PostMapping("/exit")
    public String out(@ModelAttribute CarDTO carDTO, Model model){
        String carNumber = carDTO.getCarNumber();

        boolean outChk = carService.outCheck(carNumber);
        // outCheck 메서드 결과 0 이상의 값이 리턴됐다 = 주차되어있다 = true

        if (! outChk) { // 등록되지 않은 번호
            String msg = "등록되지 않은 번호입니다";
            model.addAttribute("msg", msg);
            return "index"; // index.jsp로 이동
        }

        try {
            carService.exitCar(carDTO);
        } catch(Exception e) {
            String msg = "오류가 발생했습니다.";
            model.addAttribute("msg", msg);
            return "index"; // index.jsp로 이동
        }

        CarDTO dto = carService.findByCN(carNumber);

        model.addAttribute("car", dto);
        return "checkout"; // checkout.jsp : 정산페이지
//        return "redirect:/car/checkout"; // checkout.jsp : 정산페이지

    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam("carNumber") String carNumber, Model model){
        CarDTO carDTO = carService.findByCN(carNumber);
        model.addAttribute("car", carDTO);
        return "checkout"; // parkingLot.jsp로 이동
    }
}
