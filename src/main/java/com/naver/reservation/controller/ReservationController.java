package com.naver.reservation.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.naver.reservation.confirmreservation.dto.ConfirmReservation;
import com.naver.reservation.confirmreservation.dto.ReservationPrice;
import com.naver.reservation.detail.dto.DetailInfo;
import com.naver.reservation.detail.dto.FileName;
import com.naver.reservation.detail.dto.ProductPrice;
import com.naver.reservation.detail.dto.ProductSpecificInfo;
import com.naver.reservation.main.dto.MainProduct;
import com.naver.reservation.reservation.dto.ReservationInfo;
import com.naver.reservation.reservation.dto.ReservationInfoPrice;
import com.naver.reservation.reservation.dto.ReservationPageInfo;
import com.naver.reservation.service.ConfirmReservationPageService;
import com.naver.reservation.service.DetailPageService;
import com.naver.reservation.service.LoginPageService;
import com.naver.reservation.service.MainPageService;
import com.naver.reservation.service.ReservationPageService;
import com.naver.reservation.service.WriteReviewPageService;
import com.naver.reservation.writereview.dto.FileInfo;
import com.naver.reservation.writereview.dto.ProductDescription;
import com.naver.reservation.writereview.dto.ReservationUserComment;

@Controller
public class ReservationController {

	@Autowired
	MainPageService mainPageService;

	@Autowired
	DetailPageService detailPageService;

	@Autowired
	ReservationPageService reservationPageservice;

	@Autowired
	LoginPageService loginPageService;

	@Autowired
	ConfirmReservationPageService confirmReservationPageService;

	@Autowired
	WriteReviewPageService writeReviewPageService;

	@GetMapping("/main")
	public String getRequestMain(ModelMap model, HttpSession session) {
		String email = (String) session.getAttribute("email");

		if (email != null && !email.equals("null")) {
			model.addAttribute("email", email);
		} else {
			session.setAttribute("email", "null");
		}

		List<MainProduct> promotionInfo = mainPageService.getPromotionInfo("th");
		model.addAttribute("promotionInfo", promotionInfo);

		List<MainProduct> product = mainPageService.getProductAll(0);

		model.addAttribute("product", product);

		return "main";
	}

	@GetMapping(path = "/detail/{id}")
	public String getRequestDetail(@PathVariable(name = "id") int id, ModelMap model,
			@SessionAttribute("email") String loginEmail, HttpSession session) {
		if (!loginEmail.equals("null")) {
			model.addAttribute("email", loginEmail);
		}
		session.setAttribute("productId", id);

		List<DetailInfo> detail = detailPageService.getDetailInfo(id);
		List<ProductPrice> productPrices = detailPageService.getProductPrices(id);
		List<ProductSpecificInfo> reviewList = detailPageService.getReview(id);
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

		DecimalFormat form = new DecimalFormat("#.#");
		model.addAttribute("reviewRate", form.format(reviewRate));

		model.addAttribute("detail", detail);
		int count = detailPageService.getDetailInfoCount(id);
		model.addAttribute("count", count);
		model.addAttribute("productPrices", productPrices);
		model.addAttribute("review", reviewList);
		model.addAttribute("reviewSize", reviewSize);

		return "detail";
	}

	@GetMapping(path = "/review")
	public String moreReview(ModelMap model, @SessionAttribute("productId") int id) {

		List<DetailInfo> detail = detailPageService.getDetailInfo(id);
		List<ProductSpecificInfo> reviewList = detailPageService.getReview(id);
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

		DecimalFormat form = new DecimalFormat("#.#");
		model.addAttribute("reviewRate", form.format(reviewRate));
		model.addAttribute("review", reviewList);
		model.addAttribute("reviewSize", reviewSize);
		model.addAttribute("detail", detail);
		return "review";
	}

	@GetMapping(path = "/reservation")
	public String getRequestReservation(@SessionAttribute(name = "productId") int id, ModelMap model,
			@SessionAttribute("email") String email, HttpSession session) {
		List<ReservationPageInfo> list = reservationPageservice.getReservationPageInfo(id);
		session.setAttribute("displayInfoId", list.get(0).getDisplayInfoId());

		String[] openingDate = list.get(0).getOpeningHours().split("\\n");

		model.addAttribute("reservationPageInfo", list);
		model.addAttribute("openingDate", openingDate);

		if (!email.equals("null")) {
			model.addAttribute("email", email);
		}

		return "reservation";
	}

	@PostMapping(path = "/reservationAction")
	public String postRequestReservation(HttpServletRequest request, @SessionAttribute("email") String email,
			HttpSession session) {

		Enumeration<String> names = request.getParameterNames();
		ReservationInfo reservationInfo = new ReservationInfo();
		List<Integer> productPriceId = new ArrayList<>();
		List<Integer> count = new ArrayList<>();

		String reservationEmail = null;
		Date reservationDate = new Date();
		int cancelFlag = 0;
		Date createDate = reservationDate;
		Date modifyDate = reservationDate;
		int usedFlag = 0;

		String parameterValue;
		reservationInfo.setDisplayInfoId((Integer) session.getAttribute("displayInfoId"));
		session.removeAttribute("displayInfoId");
		reservationInfo.setProductId((Integer) session.getAttribute("productId"));

		while (names.hasMoreElements()) {
			String string = (String) names.nextElement();

			switch (string) {
			case "reservationName": {
				parameterValue = request.getParameter(string);
				reservationInfo.setReservationName(parameterValue);
				break;
			}
			case "reservationTel": {
				parameterValue = request.getParameter(string);
				reservationInfo.setReservationTel(parameterValue);
				break;
			}
			case "reservationEmail": {
				reservationEmail = request.getParameter(string);
				reservationInfo.setReservationEmail(reservationEmail);
				break;
			}
			default: {
				parameterValue = request.getParameter(string);
				productPriceId.add(Integer.parseInt(string));
				count.add(Integer.parseInt(parameterValue));
			}
			}

		}
		reservationInfo.setCancelFlag(cancelFlag);
		reservationInfo.setUsedFlag(usedFlag);
		reservationInfo.setReservationDate(reservationDate);
		reservationInfo.setCreateDate(createDate);
		reservationInfo.setModifyDate(modifyDate);



		List<ReservationInfoPrice> reservationInfoPriceList = new LinkedList<>();

		for (int i = 0; i < productPriceId.size(); i++) {
			ReservationInfoPrice reservationInfoPrice = new ReservationInfoPrice();
			reservationInfoPrice.setProductPriceId(productPriceId.get(i));
			reservationInfoPrice.setCount(count.get(i));
			reservationInfoPriceList.add(reservationInfoPrice);
		}
		List<ReservationInfo> result = new ArrayList<>(); 
		result = reservationPageservice.reservation(reservationInfo, reservationInfoPriceList, result);
		session.setAttribute("reservationId", result.get(0).getId());		

		session.setAttribute("email", reservationEmail);

		return "redirect:login";

	}

	@GetMapping(path = "login")
	public String getRequestLogin(@SessionAttribute("email") String email) {
		if (!email.equals("null")) {
			return "redirect:confirmReservation";
		} else {
			return "login";
		}
	}

	@PostMapping(path = "login")
	public String postRequestLogin(ModelMap model, @RequestParam("email") String email, HttpSession session) {
		// DB에 email을 이용한 쿼리를 보낸 후에 받아온 정보가 존재한다면 '/confirmReservation'으로 보낸다.

		int reservationCount = loginPageService.getReservationCount(email);
		if (0 < reservationCount) {
			session.setAttribute("email", email);
			return "redirect:confirmReservation";
		}

		model.addAttribute("error", 1);
		return "login";
	}

	@GetMapping(path = "/confirmReservation")
	public String getRequestConfirmReservation(ModelMap model, @SessionAttribute("email") String email) {
		if (email.equals("null")) {
			return "redirect:login";
		}

		List<ConfirmReservation> availableReservation = new ArrayList<>();
		List<ConfirmReservation> usedReservation = new ArrayList<>();
		List<ConfirmReservation> canceledReservation = new ArrayList<>();

		List<ConfirmReservation> confirmReservationList = confirmReservationPageService.getConfirmReservation(email);
		confirmReservationList.stream().forEach(v -> {
			List<ReservationPrice> list = confirmReservationPageService.getReservationPrice(v.getId());
			int totalPrice = 0;
			String history = "";

			for (ReservationPrice object : list) {

				String priceTypeName = object.getPriceTypeName();
				int count = object.getCount();
				int price = object.getPrice();
				int disCountRate = object.getDiscountRate();

				totalPrice += price * (100 - disCountRate) / 100 * count;
				history += priceTypeName + "석" + Integer.toString(count) + "개, ";
			}
			history = history.substring(0, history.lastIndexOf(","));
			v.setTotalPrice(totalPrice);
			v.setHistory(history);

			if (v.getUsedFlag() != 0) {
				usedReservation.add(v);
			} else if (v.getCancelFlag() != 0) {
				canceledReservation.add(v);
			} else {
				availableReservation.add(v);
			}
		});

		model.addAttribute("availableReservation", availableReservation);
		model.addAttribute("usedReservation", usedReservation);
		model.addAttribute("canceledReservation", canceledReservation);
		model.addAttribute("email", email);
		return "confirmReservation";

	}

	@PostMapping(path = "/confirmReservation")
	public String postRequestconfirmReservation(@RequestParam("reservationId") int id,
			@RequestParam("cancelFlag") int cancelFlag) {
		if (cancelFlag != 0) {
			confirmReservationPageService.cancelingReservation(id);
		}

		return "redirect:confirmReservation";
	}

	@GetMapping(path = "logout")
	public String getReqeustlogout(HttpSession session) {
		session.setAttribute("email", "null");

		return "redirect:login";
	}

	@GetMapping(path = "/writeReview")
	public String getRequestWriteReview(@RequestParam("productId") int productId,
			@RequestParam("reservationId") int reservationId, ModelMap model) {
		// id받아서 product description 받기
		List<ProductDescription> list = writeReviewPageService.getProductDescriptions(productId);
		String description = list.get(0).getDescription();
		model.addAttribute("description", description);
		model.addAttribute("productId", productId);
		model.addAttribute("reservationId", reservationId);

		return "writeReview";
	}

	@PostMapping(path = "/writeReview")
	@ResponseBody
	public Map<String,Object> postRequestWriteReview(HttpSession session, @RequestParam("productId") int productId,
			@RequestParam(value = "reservationId") Integer reservationId, @RequestParam(value = "score") Integer score,
			@RequestParam(value = "comment") String comment,
			@RequestParam(value = "fileList", required = false) MultipartFile[] files) {

		session.setAttribute("productId", productId);

		ReservationUserComment reservationUserComment = new ReservationUserComment();
		reservationUserComment.setProductId(productId);
		reservationUserComment.setReservationInfoId(reservationId);
		reservationUserComment.setScore(score);
		reservationUserComment.setComment(comment);
		
		Map<String,Object> result = writeReviewPageService.writeReview(reservationUserComment, files);
		return result;
		


	}

}
