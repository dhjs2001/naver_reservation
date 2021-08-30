<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/static/css/detail.css">
</head>
<body>
	<div class="review-header" onclick="history.back()"><span>←</span> 오디컴퍼니 주식회사</div>
	<div class="review-container">
		<div class="review-summary">
			<span>예매자 한줄평</span><br> <div class="filled-stars">★</div><div
				class="empty-stars">★</div> <span class="average-rate">${reviewRate }</span><span
				style="color: gray">/</span><span class="max-rate">5.0</span>
			<div class="number-box">
				<span class="number">${reviewSize }</span>건 등록
			</div>
		</div>
		<c:forEach var="item" items="${review }">
			<div class="review">
				<div class="review-content">
					<span>${detail[0].description }</span><br> <span>${item.comment }</span>
				</div>
				<div class="user-footprint">
					<span class="user-point">${item.score }</span> ㅣ <span
						class="user-email">${item.reservationEmail }</span> ㅣ <span
						class="visiting-date">${item.createDate }방문</span>
				</div>
			</div>
		</c:forEach>
		<div class="review-page-comment">
			<img alt="종 모양" src="/static/img/bell.png"> <span>네이버
				예약을 통해 실제 방문한 이용자가 남긴 평가입니다.</span>
		</div>
	</div>
</body>
<script src="/static/js/review.js"></script>
</html>