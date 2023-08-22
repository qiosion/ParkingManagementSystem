package com.kes.ParkingManagement.service;

import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    // 입차
    public int parkCar(CarDTO carDTO) {
        String carNumber = carDTO.getCarNumber();

        // 기존에 주차한 차량인지 확인
        int inChk = carRepository.inCheck(carNumber);

        if (inChk > 0) { // 이미 주차한 차량이라면 에러 처리
            return -1; // 주차 실패
        } else {
            carDTO.setEntryTime(LocalDateTime.now()); // 입차시간에 현재시각을 입력
            return carRepository.parkCar(carDTO);
        }
    }

    // 주차내역 모두 출력
    public List<CarDTO> viewAll() {
        return carRepository.viewAll();
    }

    // 현재 주차내역 출력
    public List<CarDTO> viewNow() {
        return carRepository.viewNow();
    }

    // 출차 시 등록된 차량인지 확인
    public boolean outCheck(String carNumber) {
        return carRepository.outCheck(carNumber) > 0;
    }

    // 출차 처리
    public int exitCar(CarDTO carDTO) {
        String carNumber = carDTO.getCarNumber();

        // 기존에 주차된 차량인지 확인
        int outChk = carRepository.outCheck(carNumber);

        if (outChk == 0) { // 주차되지 않은 차량이라면 에러 처리
            return -1;
        } else {
            CarDTO dto = findByCN(carDTO.getCarNumber());
            dto.setExitTime(LocalDateTime.now());
            carRepository.exitCar(dto); // 출차 처리

            LocalDateTime entryTime = dto.getEntryTime();
            LocalDateTime exitTime = dto.getExitTime();

            if (entryTime != null && exitTime != null) {
                long parkingMinutes = Duration.between(entryTime, exitTime).toMinutes();
                dto.setParkingDuration(parkingMinutes);

                carRepository.updateParkingDuration(dto); // 주차시간 계산
            }
            return 1;
        }

    }

    // 출차 처리
    public void exitCar2(CarDTO carDTO) {
        String carNumber = carDTO.getCarNumber();

        CarDTO dto = findByCN(carDTO.getCarNumber());
        dto.setExitTime(LocalDateTime.now());
        carRepository.exitCar(dto); // 출차 처리

        LocalDateTime entryTime = dto.getEntryTime();
        LocalDateTime exitTime = dto.getExitTime();

        if (entryTime != null && exitTime != null) {
            long parkingMinutes = Duration.between(entryTime, exitTime).toMinutes();
            dto.setParkingDuration(parkingMinutes);

            carRepository.updateParkingDuration(dto); // 주차시간 계산
        }
    }

    // 정산 내역 (상세정보) carNumber 로 찾기
    public CarDTO findByCN(String carNumber) {
        return carRepository.findByCN(carNumber);
    }

}
