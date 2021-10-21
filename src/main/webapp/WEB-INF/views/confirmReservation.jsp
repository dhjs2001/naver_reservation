<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예매확인 페이지</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css"
	href="/static/css/confirmReservation.css">
</head>
<body>
	<div class="container">
		<header>
			<div class="top">
				<div class="img-container">
					<a href="/main"><img alt="네이버 예약 로고"
						src="/getImage/img/whiteNaverReservationLogo.png"></a>
				</div>
				<div class="email">${email }</div>
			</div>
			<ul class="reservation-count">
				<li><img alt="달력모양" src="/getImage/img/calender2.png"> 전체<br>
					<span class="count">${availableReservation.size()+usedReservation.size() + canceledReservation.size() }</span></li>
				<li><img alt="달력모양에 시계" src="/getImage/img/calenderClock.png">
					이용예정<br> <span class="count">${availableReservation.size() }</span></li>
				<li><img alt="상자 안에 체크표시" src="/getImage/img/checkBox.png">
					이용완료<br> <span class="count">${usedReservation.size() }</span></li>
				<li><img alt="취소" src="/getImage/img/cancel2.png"> 취소·환불<br>
					<span class="count">${canceledReservation.size() }</span></li>
			</ul>
		</header>
		<ul>
			<li class="availableReservation">
				<div class="card">
					<div class="greencard-left card-parts"></div>
					<div class="greencard-bottom bottom card-parts">
						<div class="bottom-inner">
							<img alt="시계표시" src="/getImage/img/clock2.png"><span>
								예약 확정</span>
						</div>
					</div>
					<div class="greencard-right card-parts"></div>
				</div>
				<div class="item-container">
					<c:forEach var="item" items="${availableReservation }">
						<div class="item">
							<div class="item-header">
								no.${item.getId() }<br> <span class="description">${item.getDescription() }</span>
							</div>
							<hr>
							<span>일정</span> ${item.getOpeningHours() }<br> <span>내역</span>
							${item.getHistory() }<br> <span>장소</span>
							${item.getPlaceName() }<br>
							<hr>
							<div class="item-footer">
								결제 예정금액 <span class="price"><fmt:formatNumber
										type="number" pattern="###,###,###,###,###,###"
										value="${item.getTotalPrice()}" /> 원</span>
							</div>
							<form action="confirmReservation" method="post">
								<input type="hidden" name="reservationId" value="${item.getId() }">
								<input type="hidden" name="cancelFlag" value="1">
								<button class="button" type="submit">취소</button>
							</form>
						</div>
					</c:forEach>
				</div>
			</li>


			<li class="usedReservation">
				<div class="card">
					<div class="graycard-left card-parts"></div>
					<div class="graycard-bottom bottom card-parts">
						<div class="bottom-inner">
							<img alt="시계표시" src="/getImage/img/clock2.png"><span>
								이용 완료</span>
						</div>
					</div>
					<div class="graycard-right card-parts"></div>
				</div>
				<div class="item-container">
					<c:forEach var="item" items="${usedReservation }">
						<div class="item">
							<div class="item-header">
								no.${item.getId() }<br> <span class="description">${item.getDescription() }</span>
							</div>
							<hr>
							<span>일정</span> ${item.getOpeningHours() }<br> <span>내역</span>
							${item.getHistory() }<br> <span>장소</span>
							${item.getPlaceName() }<br>
							<hr>
							<div class="item-footer">
								결제 예정금액 <span class="price"><fmt:formatNumber
										type="number" pattern="###,###,###,###,###,###"
										value="${item.getTotalPrice()}" /> 원</span>
							</div>
							<form action="writeReview" method="get">
								<input type="hidden" name="reservationId" value="${item.getId() }">
								<input type="hidden" name="productId" value="${item.getProductId() }">
								<button class="button" type="submit">예매자 리뷰 남기기</button>
							</form>
						</div>
					</c:forEach>
				</div>
			</li>


			<li class="canceledReservation">
				<div class="card">
					<div class="graycard-left card-parts"></div>
					<div class="graycard-bottom bottom card-parts">
						<div class="bottom-inner">
							<img alt="시계표시" src="/getImage/img/clock2.png"> <span>
								취소·환불</span>
						</div>
					</div>
					<div class="graycard-right card-parts"></div>
				</div>
				<div class="item-container">
					<c:forEach var="item" items="${canceledReservation }">
						<div class="item">
							<div class="item-header">
								no.${item.getId() }<br> <span class="description">${item.getDescription() }</span>
							</div>
							<hr>
							<span>일정</span> ${item.getOpeningHours() }<br> <span>내역</span>
							${item.getHistory() }<br> <span>장소</span>
							${item.getPlaceName() }<br>
							<hr>
							<div class="item-footer">
								결제 예정금액 <span class="price"><fmt:formatNumber
										type="number" pattern="###,###,###,###,###,###"
										value="${item.getTotalPrice()}" /> 원</span>
							</div>
						</div>
					</c:forEach>
				</div>
			</li>
		</ul>
		<a href="/logout"><button type="button">로그아웃</button></a>
	</div>
	<script type="text/javascript" src="/static/js/confirmReservation.js"></script>
</body>
</html>