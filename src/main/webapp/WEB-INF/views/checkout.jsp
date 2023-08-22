<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<c:set var="formattedEntryTime" value="${car.entryTime.format(DateTimeFormatter.ofPattern('yyyy/MM/dd HH:mm:ss'))}" />
<c:set var="formattedExitTime" value="${car.exitTime.format(DateTimeFormatter.ofPattern('yyyy/MM/dd HH:mm:ss'))}" />

<%-- 주차 요금 계산: 30분마다 1000원씩 --%>
<c:set var="parkingFee" value="${(car.parkingDuration / 30) * 1000}" />


<html>
<head>
    <title>Checkout Place</title>
    <style>
        * {
            padding: 10px;
        }
        body {
            width: 800px;
            margin: auto;
        }
        table, th, td {
            border: 1px solid black;
            border-collapse : collapse;
        }
        </style>
</head>
<body>
    <!-- 주차시간, 요금 -->

    <div>
        <h1>주차 요금 정산</h1>
        <table>
            <tr>
                <th>차번호</th>
                <th>입차</th>
                <th>출차</th>
                <th>주차시간</th>
                <th>요금</th>
            </tr>
            <tr>
                <td>${car.carNumber}</td>
                <td>${formattedEntryTime}</td>
                <td>${formattedExitTime}</td>
                <td>${car.parkingDuration}분</td>
                <td>${parkingFee}원</td>
            </tr>
        </table>
        <button>결제하기</button>
        <a href="/">목록</a>
    </div>

</body>
</html>