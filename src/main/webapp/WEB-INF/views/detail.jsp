<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="initial-scale=1, width=device-width" />
<title>상세페이지</title>
<link rel="stylesheet" type="text/css" href="/static/css/detail.css">
</head>
<body>
	<header>
		<div class="title">
			<div class="title-img-container">
				<c:forEach var="value" items="${detail }" begin="0">
					<div>
						<img class=title-img alt="배경이미지"
							src='/getImage/${value.saveFileName }'>
					</div>
				</c:forEach>
			</div>
			<div class="title-container">
				<div class="gnb">
					<a href="/main"> <img alt="네이버 예약 로고"
						src="/getImage/img/spr_bi.c753b938cfe0481b000adab9b84a1f8c.png">
					</a>
					<div class="email">
						<a href="/login"><c:choose>
								<c:when test="${not empty requestScope.email}">
						${email }
						</c:when>
								<c:otherwise>예매확인</c:otherwise>
							</c:choose></a>
					</div>

				</div>
				<div class="title-img-page">
					<span class='current-page-number'>?</span>/<span
						class="total-page-number">${count }</span>
				</div>
				<div class="img-changer-container">
					<div class="left-mover"><</div>
					<div class="img-title-name">${detail[0].description }</div>
					<div class="right-mover">></div>
				</div>
			</div>
		</div>
	</header>
	<div class="product-info">
		<div class="product-description">
			<div class="product-description-detail animation">${detail[0].content}</div>
		</div>
		<div class="more-description">
			펼쳐보기<span>∨</span>
		</div>
	</div>
	<div class="event-info">
		<div>
			<img alt="선물상자" src="/getImage/img/giftBox.png"><span>
				이벤트 정보</span>
		</div>
		<hr>
		<div>
			<span>[네이버 예약 특별할인]</span><br> <span id="event"> <c:forEach
					var="item" items="${productPrices }">
					<c:if test="${item.discountRate != 0}">
			${item.priceTypeName}석 ${item.discountRate }%,
			</c:if>
				</c:forEach>
			</span> 할인
		</div>
	</div>
	<button class="reservation-button"
		onclick="location.href='/reservation'">
		<img alt="달력사진" src="/getImage/img/calender.png"><span>예매하기</span>
	</button>
	<div class="review-container">
		<div class="review-summary">
			<span>예매자 한줄평</span><br>
			<div class="filled-stars">★</div><div class="empty-stars">★</div>
			<span class="average-rate">${reviewRate }</span><span
				style="color: gray">/</span><span class="max-rate">5.0</span>
			<div class="number-box">
				<span class="number">${reviewSize }</span>건 등록
			</div>
		</div>
		<c:forEach var="item" items="${review }" end="4">
			<div class="review">
				<div class="review-content">
					<span>${detail[0].description }</span><br> <span>${item.comment }</span>
				</div>
				<div class="user-footprint">
					<span class="user-point">${item.score }</span> ㅣ <span
						class="user-email">${item.reservationEmail }</span> ㅣ <span
						class="visiting-date">${item.createDate }방문</span>
				</div>
				<c:if test="${item.saveFileName != null }">
					<div class="review-img">
						<c:forEach var="saveFileName" items="${item.saveFileName }">
							<img src="/getImage/${saveFileName }">
						</c:forEach>
					</div>
				</c:if>
			</div>
		</c:forEach>
		<div class="review-page-comment">
			<img alt="종 모양" src="/getImage/img/bell.png"> <span>네이버
				예약을 통해 실제 방문한 이용자가 남긴 평가입니다.</span>
		</div>
	</div>
	<button class="more-review" onclick="location.href='/review'">
		예매자 한줄평 더보기<span>→</span>
	</button>
	<div class="detail-or-map-container">
		<ul class="detail-and-map-tab">
			<li>상세정보</li>
			<li>오시는 길</li>
		</ul>
		<div class="detail-or-map">
			[소개]<br> 설명설명설명설명설명설명설명설명설명설명설명설명설명설명설명설명 <br> <br>
			[공지사항] <img alt="공지사항" src="">
		</div>
	</div>
	<footer>
		<div class="top" onclick="location.href='#'">
			<span>↑</span> TOP
		</div>
		<br>
		<div class="footer-detail">
			네이버(주)는 통신판매의 당사자가 아니며, 상품의정보, 거래조건, 이용 및 환불 등과 관련한 의무와 책임은 각 회원에게
			있습니다.<br> <br>@NAVER Corp.
		</div>

	</footer>
	<script type="text/javascript"
		src="https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js"></script>
	<script type="text/javascript" src="/static/js/detail.js"></script>
	<script type="template/handlebars" id="detail-info">
	<div class="detail-info-description">
		<div class="description">
			<span>[소개]</span><br><br>
			{{content}}
		</div>
		<div class="notice">
			<span>[공지사항]</span>
			<img alt="공지사항" src="/getImage/img/notice.jpg">
		</div>
	</div>
	</script>
	<script type="template/handlebars" id="road-map">
		<div class="road-map-img-container">
			<img alt="오시는 길" src="/getImage/{{saveFileName}}">
		</div>
		<div class="address">
			<div class="address-title">{{description}}</div>
			
			<div class="address-detail">
				<img alt="gps모양" src="/getImage/img/location.png">
				<div>
					{{placeStreet}}<br>
					<span>지번</span> {{placeLot}}<br>
					<span>{{placeName}}<span>
				</div>
			</div>
			<div class="address-phone-number">
				<img alt="전화" src="/getImage/img/phone.png"><span>{{tel}}</span>
			</div>
		</div>
		<div class="navigation-bar">
			<div class="seeking-way"><img alt="길찾기" src="/getImage/img/seekingRoad.png"> 길찾기</div>
			|<div class="navigation"><img alt="네비게이션" src="/getImage/img/navigation.png"> 네비게이션</div>
		</div>
	</script>
</body>
</html>