package com.kes.ParkingManagement.repository;

import com.kes.ParkingManagement.dto.CarDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CarRepository {
    private final SqlSessionTemplate sql; // 자바 클래스와 매퍼 간의 연결을 해줌

    // 입차
    public int parkCar(CarDTO carDTO) {
        /*
        첫번째 파라미터 Car.parkCar
        - 이때 Car는 carMapper.xml에서 작성한 namespace
        - parkCar는 insert에서 설정된 id값
        두번째 파라미터 carDTO : carMapper로 넘기는 값
         */
        return sql.insert("Car.parkCar", carDTO);
    }

    public int inCheck(String carNumber) {
        return sql.selectOne("Car.inCheck", carNumber);
    }

    // 주차내역 모두 출력
    public List<CarDTO> viewAll() {
        return sql.selectList("Car.viewAll");
    }

    // 현재 주차내역 출력
    public List<CarDTO> viewNow() {
        return sql.selectList("Car.viewNow");
    }

    // 출차
    public void exitCar(CarDTO carDTO) {
        sql.update("Car.exitCar", carDTO);
    }

    public int outCheck(String carNumber) {
        return sql.selectOne("Car.outCheck", carNumber);
    }

    // 정산 내역 (상세정보)
    public CarDTO findByCN(String carNumber) {
        return sql.selectOne("Car.findByCN", carNumber);
    }

    // 주차시간 계산
    public void updateParkingInformation(CarDTO carDTO) {
        sql.update("Car.updateParkingInformation", carDTO);
    }

    // 페이징
    public List<CarDTO> pagingList(Map<String, Integer> pagingParams) {
        return sql.selectList("Car.pagingList", pagingParams);
    }

    public int carCount() {
        return sql.selectOne("Car.carCount");
    }

    public CarDTO findByPN(Long parkNumber) {
        return sql.selectOne("Car.findByPN", parkNumber);
    }
}
