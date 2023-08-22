package com.kes.ParkingManagement.repository;

import com.kes.ParkingManagement.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final SqlSessionTemplate sql; // 자바 클래스와 매퍼 간의 연결을 해줌

    // 결제 내역 등록
    public int createOrder(OrderDTO orderDTO) {
        return sql.insert("Order.createOrder", orderDTO);
    }


}
