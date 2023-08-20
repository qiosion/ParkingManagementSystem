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
    public String in(@ModelAttribute CarDTO carDTO){
        String carNumber = carDTO.getCarNumber(); // 차번호
        System.out.println("입차번호 : " + carNumber);

        int parkResult = carService.parkCar(carDTO);
        if (parkResult > 0) { // 주차 성공
            System.out.println("입차시간설정 : " + carDTO.getEntryTime());
            return "redirect:/car/list";
        } else { // 주차 실패
            return "index"; // index.jsp로 이동
        }
    }

    /*
    메서드명 : list
    매개변수
        - CarDTO : 자동차 정보
        - model
    메서드 설명 : 전체 주차 정보를 조회
    carDTOList :
        입차/출차 된 차의 내역을 모두 출력.
        parkingLot.jsp 에서 carList 를 통해 사용가능.
    */
    @GetMapping("/list")
    public String carList(@ModelAttribute CarDTO carDTO, Model model){
        List<CarDTO> carDTOList = carService.viewAll();
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
        System.out.println("출차");

        carService.exitCar(carDTO);

        CarDTO dto = carService.findByCN(carDTO.getCarNumber());

        model.addAttribute("car", dto);
        return "checkout"; // checkout.jsp : 정산페이지
//        return "redirect:/car/checkout"; // checkout.jsp : 정산페이지

        /*
        carDTO.setExitTime(LocalDateTime.now());
        carDTO.setState("출차");

        여기서 문제점 ! 같은차번호라면?
        흠..차번호가 있는지 없는지 확인할 때 state='입차'인 것에서만 골라야하겠네

        // 출차 처리 및 정산 내역
        CarDTO exitResult = carService.exitCarAndView(carDTO);
        if (exitResult != null) { // 출차 성공
            System.out.println("출차시간설정 : " + carDTO.getExitTime());
            model.addAttribute("car", exitResult);
        } else { // 출차 실패
            return "index"; // index.jsp로 이동
        }
        */
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam("carNumber") String carNumber, Model model){
        CarDTO carDTO = carService.findByCN(carNumber);
        model.addAttribute("car", carDTO);
        return "checkout"; // parkingLot.jsp로 이동
    }
}
