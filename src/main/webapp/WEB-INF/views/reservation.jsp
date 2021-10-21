<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>예약페이지</title>
<link href="/static/css/reservation.css" type="text/css"
	rel="stylesheet">
</head>
<body>
	<header>
		<div class="top">
			<span>←</span>
			<div class="title">${reservationPageInfo[0].description }</div>
		</div>
		<div class="product-img-container">
			<img src="/getImage/img/${reservationPageInfo[0].fileName }">
			<div class="product-summarization">
				<div class="description">${reservationPageInfo[0].description }</div>
				<div class="summarize-info">
					￦${reservationPageInfo[0].price }~<br>${openingDate[0] }</div>
			</div>
		</div>
	</header>
	<section class="product-info">
		<article>
			<div>
				<h3>${reservationPageInfo[0].description }</h3>
				장소 : ${reservationPageInfo[0].placeStreet }<br> <br>기간:
				${openingDate[0] }
			</div>
		</article>
		<article>
			<h3>관람시간</h3>
			<c:forEach var="item" items="${openingDate }" begin="1">
					${item }<br>
				<br>
			</c:forEach>
		</article>
		<article>
			<div>
				<h3>요금</h3>
				<c:forEach var="item" items="${reservationPageInfo }">
					${item.priceTypeName }석 
					<fmt:formatNumber
									type="number" pattern="###,###,###,###,###,###"
									value="${item.price}" />원<br>
				</c:forEach>
			</div>
		</article>
	</section>
	<form id='reservation-form' action="/reservationAction" method="POST" onsubmit='return purChaserInfo.checkBeforeSubmit()'>
		<article class="tikets">
			<c:forEach var="item" items="${reservationPageInfo }">
				<div class="tikets-kind">
					<div>
						<b>${item.priceTypeName }석</b><br> <b><span class="price"><fmt:formatNumber
									type="number" pattern="###,###,###,###,###,###"
									value="${item.price * (100-item.discountRate) / 100 }" /></span>원</b><br>
						<span><fmt:formatNumber type="number"
								pattern="###,###,###,###,###,###"
								value="${item.price * (100-item.discountRate) / 100 }" />
							원(${item.discountRate}% 할인가)</span>
					</div>
					<div>
						<div class="tiket-number-button-container">
							<img class="tiket-number-button left-button" alt="-버튼"
								src="/getImage/img/nonColorMinusButton.png"> <input
								class="tikets-input" type="text" name="${item.productPriceId }"
								value=0> <img class="tiket-number-button right-button"
								alt="+버튼" src="/getImage/img/colorPlusButton.png"><br>
						</div>
						<span class="total-price">0원</span>
					</div>

				</div>
			</c:forEach>
		</article>
		<article class="purchaser-info">
			<div class="purchaser-info-title">
				<h3>예매자 정보</h3>
				<div>
					<img alt="체크표시" src="/getImage/img/check.png">필수입력
				</div>
			</div>
			<div class="specific-info">
				<div class="specific-info-label">
					<label for="reservation-name"><img alt="체크표시"
						src="/getImage/img/check.png">예매자</label><br> <label
						for="reservation-tel"><img alt="체크표시"
						src="/getImage/img/check.png">연락처</label><br> <label
						for="reservation-email"><img alt="체크표시"
						src="/getImage/img/check.png">이메일</label><br> 예매내용
				</div>
				<div class="specific-info-input">
					<input id="reservation-name" type="text" required placeholder="네이버"
						name="reservationName"><br> <input
						id="reservation-tel" type="text" placeholder="휴대폰 입력 시 예매내역 문자발송"
						name="reservationTel"><br> <input
						id="reservation-email" type="text"
						placeholder="crong@codesquad.kr" name="reservationEmail"
						<c:if test='${email !="null" }'>
						value = ${email }
						</c:if>
						><br>
					<span class="date"></span>, 총 <span
						class="tikets-sum">0매</span>
				</div>
			</div>
		</article>
		
		
		<article class="terms">
			<div class="check-terms">
				<input id='check-terms-checkbox' type="checkbox"><label for='check-terms-checkbox'></label> 이용자 전체 동의 <span>필수동의</span>
			</div>
			<div class="personal-info-terms">
				┖개인정보 수집 및 이용 동의<span class="terms-span">보기<img
					alt="위가 열린 꺾쇠" src="/getImage/img/openTopClamp.png"></span>
				<div class="specific-personal-info-terms">개인정보 관련 내용내용개인정보 관련
					내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용 개인정보 관련
					내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용
					개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련
					내용내용 개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련
					내용내용개인정보 관련 내용내용</div>
			</div>
			<div class="personal-info-terms">
				┖개인정보 제3자 제공 동의<span class="terms-span">보기<img
					alt="위가 열린 꺾쇠" src="/getImage/img/openTopClamp.png"></span>
				<div class="specific-personal-info-terms">개인정보 관련 내용내용개인정보 관련
					내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용 개인정보 관련
					내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용
					개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련
					내용내용 개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련 내용내용개인정보 관련
					내용내용개인정보 관련 내용내용</div>
			</div>
		</article>
		<button id="reservation-button" type="submit"><img alt="박스 안에 들어있는 N" src="/getImage/img/whiteNaverBox.png">예약하기</button>
	</form>
	
	${httpRequest.reservationPageInfo[0] }
	
	<script type="text/javascript" src="/static/js/reservation.js"></script>
	<Script>
		
	</Script>
</body>
</html>