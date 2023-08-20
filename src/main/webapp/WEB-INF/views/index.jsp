<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Parking Management System</title>
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
    <script>
        var msg = "${msg}";
        if (msg !== "") { // 에러메시지가 있을 경우 팝업
            alert(msg);
        }
    </script>
</head>
<body>
    <!-- 하나의 form 에서 2개의 액션을 사용하기 위해
     input type="submit"으로 두개를 만들 되,
     각각 onclick 이벤트를 주었음 -->

    <div>
        <h1>주차 관리 시스템</h1>
        <form id="form" method="post">
            <input type="text" name="carNumber" placeholder="차번호"><br>
            <input type="submit" id="park" value="주차" onclick='javascript: form.action="/car/park"'>
            <input type="submit" id="exit" value="출차" onclick='javascript: form.action="/car/exit"'>
           <!-- <button type="button" id="exit">출차</button> -->
        </form>
    </div>


    <!-- 주차 목록 보여줌 -->
    <div>
        <h3>전체 주차 목록</h1>
        <table>
            <tr>
                <th>주차번호</th>
                <th>차 번호</th>
                <th>입차시간</th>
                <th>출차시간</th>
                <th>주차시간</th>
            </tr>
            <c:forEach items="${carList}" var="car">
                <tr>
                    <td>${car.parkNumber}</td>
                    <td>${car.carNumber}</td>
                    <td>${car.entryTime}</td>
                    <td>${car.exitTime}</td>
                    <td>${car.parkingDuration}</td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <!--
    <div>
        <c:choose>
            <%-- 현재 페이지가 1페이지면 이전 글자만 보여줌 --%>
            <c:when test="${paging.page<=1}">
                <span>[이전]</span>
            </c:when>
            <%-- 1페이지가 아닌 경우에는 [이전]을 클릭하면 현재 페이지보다 1 작은 페이지 요청 --%>
            <c:otherwise>
                <a href="/car/paging?page=${paging.page-1}">[이전]</a>
            </c:otherwise>
        </c:choose>

        <%--  for(int i=startPage; i<=endPage; i++)      --%>
        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i" step="1">
            <c:choose>
                <%-- 요청한 페이지에 있는 경우 현재 페이지 번호는 텍스트만 보이게 --%>
                <c:when test="${i eq paging.page}">
                    <span>${i}</span>
                </c:when>

                <c:otherwise>
                    <a href="/car/paging?page=${i}">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:choose>
            <c:when test="${paging.page>=paging.maxPage}">
                <span>[다음]</span>
            </c:when>
            <c:otherwise>
                    <a href="/car/paging?page=${paging.page+1}">[다음]</a>
            </c:otherwise>
        </c:choose>
    </div>
    -->
</body>
</html>