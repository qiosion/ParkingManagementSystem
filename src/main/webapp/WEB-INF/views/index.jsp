<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Parking Management System</title>
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
</body>
</html>