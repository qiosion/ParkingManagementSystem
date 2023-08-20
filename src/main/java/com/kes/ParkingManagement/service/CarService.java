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
        carDTO.setEntryTime(LocalDateTime.now()); // 입차시간에 현재시각을 입력

        return carRepository.parkCar(carDTO);
//        String carNumber = carDTO.getCarNumber();
//
//        // 기존에 주차한 차량인지 확인
//        boolean carExists = carRepository.existsByCarNumber(carNumber);
//
//        if (carExists) {
//            // 이미 주차한 차량이라면 에러 처리
//            // 예: throw new CarAlreadyParkedException("이미 주차된 차량입니다.");
//            return -1; // 주차 실패를 의미하는 코드 반환
//        } else {
//            // 주차 작업 수행 및 저장
//            // ...
//            return 1; // 주차 성공을 의미하는 코드 반환
//        }
    }

    // 주차내역 모두 출력
    public List<CarDTO> viewAll() {
        return carRepository.viewAll();
    }

    // 출차 처리
    public void exitCar(CarDTO carDTO) {
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
