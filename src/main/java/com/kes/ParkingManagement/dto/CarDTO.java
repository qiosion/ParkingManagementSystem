package com.kes.ParkingManagement.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp; // 이거쓰고싶은데 Duration 계산이안됨..
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CarDTO {
    private Long parkNumber; // 주차번호
    private String carNumber; // 차번호
    private LocalDateTime entryTime; // 입차시간
    private LocalDateTime exitTime;  // 출차시간
    private Long parkingDuration; // 주차시간 = 출차시간-입차시간
    private String state; // 상태 : 입차/출차

//    public Long getParkingDuration() {
//        if (entryTime != null && exitTime != null) {
//            parkingDuration = Duration.between(entryTime, exitTime).toMinutes();
//        }
//        return parkingDuration;
//    }
}
