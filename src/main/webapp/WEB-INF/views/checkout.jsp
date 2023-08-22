<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<c:set var="formattedEntryTime" value="${car.entryTime.format(DateTimeFormatter.ofPattern('yyyy/MM/dd HH:mm:ss'))}" />
<c:set var="formattedExitTime" value="${car.exitTime.format(DateTimeFormatter.ofPattern('yyyy/MM/dd HH:mm:ss'))}" />

<%-- 주차 요금 계산: 30분마다 1000원씩 --%>
<%--  <c:set var="parkingFee" value="${(car.parkingDuration / 30) * 1000}" />  --%>


<html>
<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.iamport.kr/v1/iamport.js"></script>
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
                <td>${car.parkingFee}원</td>
            </tr>
        </table>
        <button type="button" id="orderBtn">결제하기</button>
        <a href="/"><button>목록</button></a>
    </div>
<script>
    // 결제버튼 클릭
    $("#orderBtn").on("click",function(){
        if (confirm("주차 요금을 결제하시겠습니까?")) {
            var parkNumber = ${car.parkNumber}; // 주차번호
            var amount = ${car.parkingFee}; // 결제금액
            var buyer_name = ${car.carNumber}; // 차번호
            IMP.init('imp35581825'); // 가맹점 식별코드
            IMP.request_pay({
                pg : 'kakaopay.TC0ONETIME',
                pay_method : 'kakaopay',
                merchant_uid : 'merchant' + new Date().getTime(),
                name : '주문명:결제테스트',
                amount : amount, //amount,
                buyer_name : buyer_name
            }, function(rsp) {
                if ( rsp.success ) {
                    var msg = '결제가 완료되었습니다.';
                    msg += '\n고유ID : ' + rsp.imp_uid;
                    msg += '\n상점 거래ID : ' + rsp.merchant_uid;
                    msg += '\결제 금액 : ' + rsp.paid_amount;
                    msg += '카드 승인번호 : ' + rsp.apply_num;

                    // 재고 업데이트, 출고내역 등록
                    $.ajax({
                        url: "${pageContext.request.contextPath}/order/process",
                        type: "POST",
                        data : {
                            parkNumber : parkNumber,
                            amount : amount,
                            carNumber : buyer_name},
                        success : function(){
                            alert(msg);
                            location.href = "${pageContext.request.contextPath}/"
                        },error:function(){
                            alert("실패");
                        }
                    })
                } else {
                    var msg = '결제에 실패하였습니다.';
                    msg += '에러내용 : ' + rsp.error_msg;
                    alert(msg);
                }
            });
        }
    })
</script>
</body>
</html>