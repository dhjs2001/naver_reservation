package com.naver.reservation.controller;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.reservation.confirmreservation.dto.ConfirmReservation;
import com.naver.reservation.detail.dto.DetailInfo;
import com.naver.reservation.detail.dto.DisplayInfo;
import com.naver.reservation.detail.dto.FileName;
import com.naver.reservation.detail.dto.ProductSpecificInfo;
import com.naver.reservation.main.dto.Category;
import com.naver.reservation.main.dto.MainProduct;
import com.naver.reservation.reservation.dto.ReservationInfo;
import com.naver.reservation.reservation.dto.ReservationInfoPrice;
import com.naver.reservation.reservation.dto.ReservationPageInfo;
import com.naver.reservation.service.ConfirmReservationPageService;
import com.naver.reservation.service.DetailPageService;
import com.naver.reservation.service.MainPageService;
import com.naver.reservation.service.ReservationPageService;
import com.naver.reservation.service.WriteReviewPageService;
import com.naver.reservation.writereview.dto.ReservationUserComment;

@RestController
@RequestMapping(path = "/api")
public class ReservationApiController {
	@Autowired
	MainPageService mainPageService;
	@Autowired
	DetailPageService detailPageService;
	@Autowired
	ReservationPageService reservationPageservice;
	@Autowired
	ConfirmReservationPageService confirmReservationPageService;
	@Autowired
	WriteReviewPageService writeReviewPageService;

	@GetMapping(path = "/products")
	public Map<String, Object> getProductList(@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "start", required = false) Integer start) {
		Map<String, Object> map = new HashMap<>();
		List<MainProduct> list = new ArrayList<>();
		if (categoryId != null) {
			if (start != null) {
				list = mainPageService.getProductByCategoryId(categoryId, start);
			} else {
				list = mainPageService.getProductByCategoryId(categoryId, 0);
			}
		} else {
			list = mainPageService.getProductAll(0);
		}

		map.put("product", list);
		return map;
	}

	@GetMapping(path = "/products/{id}/{start}")
	public Map<String, Object> ProductList(@PathVariable(name = "id") int id, @PathVariable(name = "start") int start) {
		Map<String, Object> map = new HashMap<>();

		if (id == 0) {
			List<MainProduct> list = mainPageService.getProductAll(start);
			Integer length = mainPageService.selectCount();
			map.put("product", list);
			map.put("length", length);

		} else if (0 < id && id < 6) {
			List<MainProduct> list = mainPageService.getProductByCategoryId(id, start);
			Integer length = mainPageService.selectCountByCategoryId(id);
			map.put("product", list);
			map.put("length", length);
		}
		return map;
	}

	// 상품전시정보
	@GetMapping(path = "/products/{displayInfoId}")
	public Map<String, Object> getDisplayInfo(@PathVariable(name = "displayInfoId") int displayInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<DetailInfo> detail = detailPageService.getDetailInfo(displayInfoId);
		List<ProductSpecificInfo> reviewList = detailPageService.getReview(displayInfoId);
		int reviewSize = reviewList.size();
		double reviewScore = 0;
		double reviewRate = 0;
		if (reviewList.isEmpty() != true) {
			for (ProductSpecificInfo i : reviewList) {
				reviewScore += i.getScore();
			}

			reviewList = reviewList.stream().map(review -> {
				String date = review.getCreateDate();
				String result = date.replace("-", ".");
				review.setCreateDate(result);
				String email = review.getReservationEmail();
				if (email != null) {
					email = email.substring(0, 4) + "****";
				}
				review.setReservationEmail(email);
				int reservationUserCommentId = review.getReservationUserCommentId();
				List<FileName> fileNameList = detailPageService.getFileNames(reservationUserCommentId);
				List<String> saveFileList = new ArrayList<>();
				fileNameList.forEach((fileName) -> {
					saveFileList.add(fileName.getSaveFileName());
				});
				review.setSaveFileName(saveFileList);
				return review;
			}).collect(Collectors.toList());

			reviewRate = reviewScore / reviewSize;
		} else {
			System.out.println("review 객체가 비었음");
		}
		List<ReservationPageInfo> list = reservationPageservice.getReservationPageInfo(displayInfoId);

		DecimalFormat form = new DecimalFormat("#.#");
		map.put("detail", detail);
		map.put("displayInfo", list);
		map.put("reviewRate", form.format(reviewRate));
		map.put("review", reviewList);
		map.put("reviewLength", reviewSize);

		return map;
	}

	// 예약 정보 조회
	@GetMapping(path = "/reservations")
	public Map<String, Object> getReservations(@RequestParam(value = "reservationEmail") String reservationEmail) {
		Map<String, Object> map = new HashMap<>();
		List<ConfirmReservation> list = confirmReservationPageService.getConfirmReservation(reservationEmail);
		map.put("reservation", list);
		return map;
	}

	// 예약하기
	@PostMapping(path = "/reservations")
	public Map<String, Object> postRequestReservations(@RequestBody HashMap<String, Object> jsonMap) {

		String[] keyList = { "displayInfoId", "prices", "productId", "reservationEmail", "reservationName",
				"reservationTelephone", "reservationYearMonthDay" };

		List<ReservationInfoPrice> reservationInfoPriceList = new ArrayList<>();
		for (String i : keyList) {
			if (jsonMap.get(i) == null) {
				jsonMap.clear();
				jsonMap.put("error", "check user json attribute : " + i);
				return jsonMap;
			}
			if (i == "prices") {
				ObjectMapper mapper = new ObjectMapper();
				reservationInfoPriceList = mapper.convertValue(jsonMap.get("prices"),
						new TypeReference<List<ReservationInfoPrice>>() {
						});
				for (int j = 0; j < reservationInfoPriceList.size(); j++) {
					if (reservationInfoPriceList.get(j).checkValue() == false) {
						jsonMap.clear();
						jsonMap.put("error", "check user json prices-attribute");
						return jsonMap;
					}
				}
			}

		}

		ReservationInfo reservationInfo = new ReservationInfo();

		int displayInfoId = (int) jsonMap.get("displayInfoId");
		int productId = (int) jsonMap.get("productId");
		String reservationEmail = (String) jsonMap.get("reservationEmail");
		String reservationName = (String) jsonMap.get("reservationName");
		String reservationTelephone = (String) jsonMap.get("reservationTelephone");
		String reservationYearMonthDay = (String) jsonMap.get("reservationYearMonthDay");

		reservationInfo.setCancelFlag(0);
		Date date = new Date();
		reservationInfo.setCreateDate(date);
		reservationInfo.setDisplayInfoId(displayInfoId);
		reservationInfo.setModifyDate(date);
		reservationInfo.setProductId(productId);

		Date reservationDate = new Date();
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			reservationDate = simpleDateFormat.parse(reservationYearMonthDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		reservationInfo.setReservationDate(reservationDate);
		reservationInfo.setReservationEmail(reservationEmail);
		reservationInfo.setReservationName(reservationName);
		reservationInfo.setReservationTel(reservationTelephone);
		reservationInfo.setUsedFlag(0);

		Map<String, Object> map = new HashMap<>();
		map = reservationPageservice.reservation(reservationInfo, reservationInfoPriceList);

		return map;

	}

	@PutMapping(path = "/reservations/{reservationId}")
	public Map<String, Object> putReqeustReservations(@PathVariable(name = "reservationId") int reservationId) {

		Map<String, Object> map = reservationPageservice.cancelReservation(reservationId);
		return map;

	}

	// 한줄평(Comment) 등록
	@PostMapping(path = "/reservations/{reservationInfoId}/")
	public Map<String, Object> postRequestReservationsComment(@RequestParam("productId") int productId,
			@PathVariable(name = "reservationInfoId") int reservationId, @RequestParam(value = "score") int score,
			@RequestParam(value = "comment") String comment,
			@RequestParam(value = "file", required = false) MultipartFile[] files) {

		ReservationUserComment reservationUserComment = new ReservationUserComment();
		reservationUserComment.setProductId(productId);
		reservationUserComment.setReservationInfoId(reservationId);
		reservationUserComment.setScore(score);
		reservationUserComment.setComment(comment);

		Map<String, Object> result = writeReviewPageService.writeReview(reservationUserComment, files);
		return result;
	}

	@GetMapping(path = "/detail/detailInfo/{displayInfoId}")
	public List<DetailInfo> getDetailInfos(@PathVariable(name = "displayInfoId") int displayInfoId) {
		List<DetailInfo> list = detailPageService.getDetailInfo(displayInfoId);
		return list;
	}

	@GetMapping(path = "/detail/roadMap/{id}")
	public List<DisplayInfo> getDisplayInfos(@PathVariable(name = "id") int id) {
		List<DisplayInfo> list = detailPageService.getDisplayInfos(id);
		return list;
	}
	
	@GetMapping(path="/categories")

	public List<Category> getRequestCategories() {
		return mainPageService.getCategories();
	}
	@GetMapping("/promotions")
	public List<Map<String,Object>> getTest(HttpServletRequest request) {
		List<MainProduct> promotionInfoList = mainPageService.getPromotionInfo("th");
		String preUrl = "http://" + request.getLocalAddr() + ":8080/getImage/";
		List<Map<String,Object>> result = new LinkedList<>();
		promotionInfoList.forEach(promotionInfo ->{
			Map<String, Object> map = new HashMap<>();
			map.put("productId", promotionInfo.getId());
			map.put("description", promotionInfo.getDescription());
			String imgUrl = preUrl + promotionInfo.getSaveFileName();
			map.put("url", imgUrl);
			result.add(map);
		});
		return result;
	}

}
