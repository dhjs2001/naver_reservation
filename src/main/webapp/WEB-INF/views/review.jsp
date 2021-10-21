<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰페이지</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/static/css/detail.css">
</head>
<body>
	<div class="review-header">
		<span onclick="document.location.href= '/detail/${sessionScope.productId}'">←</span> ${detail.get(0).getDescription()}
	</div>
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
		<c:forEach var="item" items="${review }">
			<div class="review">
				<div class="review-content">
					<span>${detail.get(0).description }</span><br> <span>${item.comment }</span>
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
</body>
<script src="/static/js/review.js"></script>
</html>