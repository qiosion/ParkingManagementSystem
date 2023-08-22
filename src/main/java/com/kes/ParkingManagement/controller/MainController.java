package com.kes.ParkingManagement.controller;

import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.dto.PageDTO;
import com.kes.ParkingManagement.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CarService carService;

    /*
    메서드명 : index
    매개변수
        - CarDTO carDTO
        - Model model
        - int page : page 값은 필수가 아니고, defaultValue는 1이다
    메서드 설명 : 인덱스 + 페이징
        - /car/paging?page=2 와 같은 방식을 통해 해당 페이지를 읽어온다
        - 처음 페이지 요청은 1페이지를 보여줌
    */
    @GetMapping("/")
    public String index(@ModelAttribute CarDTO carDTO, Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        List<CarDTO> carDTOList = carService.viewAll();
        model.addAttribute("carList", carDTOList);

        // 페이징
        // 해당 페이지에서 보여줄 글 목록
        List<CarDTO> carList = carService.pagingList(page);

        // 계산한 값
        PageDTO pageDTO = carService.pagingParam(page);

        model.addAttribute("carList", carList);
        model.addAttribute("paging", pageDTO);

        return "index";
    }
}
