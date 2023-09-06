package com.kes.ParkingManagement.controller;

import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.dto.PageDTO;
import com.kes.ParkingManagement.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        - CarDTO carDTO : 자동차 정보
        - Model model
        - RedirectAttributes redirectAttributes : index페이지로 리다이렉트 시 메시지를 띄우기 위해 사용
    메서드 설명 : 입차. input으로 넣은 차번호와 entryTime을 삽입
        - 주차번호는 자동증가
        - state는 '입차'가 디폴트값
    */
    @PostMapping("/park")
    public String in(@ModelAttribute CarDTO carDTO, Model model, RedirectAttributes redirectAttributes){
        String carNumber = carDTO.getCarNumber(); // 차번호

        int parkResult = carService.parkCar(carDTO);
        if (parkResult > 0) { // 주차 성공
            return "redirect:/car/list";
        } else if (parkResult == -1) { // 중복 차량
            String msg = "중복된 차번호로 주차할 수 없습니다";
            redirectAttributes.addFlashAttribute("msg", msg); // 메시지를 Flash 속성에 추가
            return "redirect:/";
        } else { // 주차 실패
            return "redirect:/"; // index.jsp로 이동
        }
    }

    /*
    메서드명 : list
    매개변수
        - CarDTO carDTO : 자동차 정보
        - Model model
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
        - CarDTO carDTO: 자동차 정보
        - Model model
        - RedirectAttributes redirectAttributes : index페이지로 리다이렉트 시 메시지를 띄우기 위해 사용
    메서드 설명 : 출차시도. 등록된 번호라면 주문 페이지로 이동함
    */
    @PostMapping("/exit")
    public String out(@ModelAttribute CarDTO carDTO, Model model, RedirectAttributes redirectAttributes){
        String carNumber = carDTO.getCarNumber();

        // 등록된 번호인지 확인
        CarDTO dto = carService.findByCN(carNumber);

        if (dto == null) { // 등록X 차량
            String msg = "등록되지 않은 번호입니다";
            redirectAttributes.addFlashAttribute("msg", msg); // 메시지를 Flash 속성에 추가
            return "redirect:/";
        }

        // 주문 페이지로 넘어가기
        model.addAttribute("car", dto);
        return "checkout"; // checkout.jsp : 정산페이지
    }

    /*
    메서드명 : checkout
    매개변수
        - String carNumber : 차번호
        - Model model
    메서드 설명 : 정산하기 페이지로 이동하여 입/출차 시각, 주차시간, 요금의 상세정보를 보여줌
    */
    @GetMapping("/checkout")
    public String checkout(@RequestParam("carNumber") String carNumber, Model model){
        CarDTO carDTO = carService.findByCN(carNumber);
        model.addAttribute("car", carDTO);
        return "checkout"; // parkingLot.jsp로 이동
    }

}
