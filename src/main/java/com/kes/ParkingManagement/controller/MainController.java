package com.kes.ParkingManagement.controller;

import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CarService carService;

    @GetMapping("/")
    public String index(@ModelAttribute CarDTO carDTO, Model model){
        List<CarDTO> carDTOList = carService.viewAll();
        model.addAttribute("carList", carDTOList);

        return "index";
    }
}
