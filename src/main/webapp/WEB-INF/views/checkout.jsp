<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                <td>${car.entryTime}</td>
                <td>${car.exitTime}</td>
                <td>${car.parkingDuration}</td>
                <td>${car.parkingDuration*500}</td>
            </tr>
        </table>
        <a href="/">목록</a>
    </div>

</body>
</html>