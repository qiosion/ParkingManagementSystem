package com.kes.ParkingManagement.service;

import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.dto.PageDTO;
import com.kes.ParkingManagement.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                // 주차요금 계산 : 30분마다 1천원
//                long parkingFee = (parkingMinutes / 30) * 1000;
                // 주차요금 계산 : 1분마다 10원
                long parkingFee = parkingMinutes * 10;
                dto.setParkingFee(parkingFee);

                carRepository.updateParkingInformation(dto); // 주차시간 계산
            }
            return 1;
        }

    }

    // 요금계산을 위한 임시 정보
    public CarDTO tempInfo(CarDTO carDTO) {
        String carNumber = carDTO.getCarNumber();

        // 기존에 주차된 차량인지 확인
        int outChk = carRepository.outCheck(carNumber);

        if (outChk == 0) { // 주차되지 않은 차량이라면 에러 처리
            return null;
        } else {
            CarDTO dto = findByCN(carDTO.getCarNumber());
            dto.setExitTime(LocalDateTime.now());

            LocalDateTime entryTime = dto.getEntryTime();
            LocalDateTime exitTime = dto.getExitTime();

            if (entryTime != null && exitTime != null) {
                long parkingMinutes = Duration.between(entryTime, exitTime).toMinutes();
                dto.setParkingDuration(parkingMinutes);

                // 주차요금 계산 : 30분마다 1천원
//                long parkingFee = (parkingMinutes / 30) * 1000;
                // 주차요금 계산 : 1분마다 10원
                long parkingFee = parkingMinutes * 10;
                dto.setParkingFee(parkingFee);
            }
            return dto;
        }

    }

    // 정산 내역 (상세정보) carNumber 로 찾기
    public CarDTO findByCN(String carNumber) {
        return carRepository.findByCN(carNumber);
    }

    // 페이징
    int pageLimit = 5; // 1 페이지 당 보이는 글 갯수
    int blockLimit = 3; // 하단에 보여줄 페이지 번호 갯수

    public List<CarDTO> pagingList(int page) {

        /*
        select * from parkinglot_table limit 0, 5;
        -> 0, 5, 10, ...
        -> (요청받은 페이지 - 1) * 5
        */
        int pagingStart = (page - 1) * pageLimit;

        // 숫자 2개를 담아서 보내야하므로 맵을 사용함
        Map<String, Integer> pagingParams = new HashMap<>();
        pagingParams.put("start", pagingStart);
        pagingParams.put("limit", pageLimit);

        List<CarDTO> pagingList = carRepository.pagingList(pagingParams);
        return pagingList;
    }

    public PageDTO pagingParam(int page) {
        // 전체 글 갯수 조회
        int carCount = carRepository.carCount();

        // 전체 페이지 갯수 계산 : 나누어 떨어지지 않는 경우 올림
        int maxPage = (int) (Math.ceil((double) carCount / pageLimit));

        // 시작 페이지 값 계산(1, 6, 11, ...)
        int startPage = (((int)(Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;

        // 끝 페이지 값 계산(5, 11, 16, ...)
        int endPage = startPage + blockLimit - 1;
        if (endPage > maxPage) {
            endPage = maxPage;
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setMaxPage(maxPage);
        pageDTO.setStartPage(startPage);
        pageDTO.setEndPage(endPage);

        return pageDTO;
    }

    // 주차번호로 자동차 정보 찾기
    public CarDTO findByPN(Long parkNumber) {
        return carRepository.findByPN(parkNumber);
    }
}
