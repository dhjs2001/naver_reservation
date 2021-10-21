package com.naver.reservation.service.impl;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.naver.reservation.service.DetailPageService;
import com.naver.reservation.service.WriteReviewPageService;
import com.naver.reservation.writereview.dao.FileInfoDao;
import com.naver.reservation.writereview.dao.ProductDescriptionDao;
import com.naver.reservation.writereview.dao.ReservationUserCommentDao;
import com.naver.reservation.writereview.dao.ReservationUserCommentImageDao;
import com.naver.reservation.writereview.dto.FileInfo;
import com.naver.reservation.writereview.dto.ProductDescription;
import com.naver.reservation.writereview.dto.ReservationUserComment;
import com.naver.reservation.writereview.dto.ReservationUserCommentImage;

@Service
public class WriteReviewPageServiceImpl implements WriteReviewPageService {

	@Autowired
	ProductDescriptionDao productDescriptionDao;

	@Autowired
	ReservationUserCommentDao reservationUsercommentDao;

	@Autowired
	ReservationUserCommentImageDao reservationUsercommentImageDao;

	@Autowired
	FileInfoDao fileInfoDao;

	@Autowired
	DetailPageService detailPageService;

	@Override
	@Transactional
	public List<ProductDescription> getProductDescriptions(int id) {
		List<ProductDescription> list = productDescriptionDao.getProductDescription(id);
		return list;
	};

	@Override
	@Transactional
	public int insertAction(ReservationUserComment object) {
		Date now = new Date();
		object.setCreateDate(now);
		object.setModifyDate(now);
		int key = reservationUsercommentDao.insertAction(object);
		return key;
	}

	@Override
	@Transactional
	public int insertAction(ReservationUserCommentImage object) {

		int key = reservationUsercommentImageDao.insertAction(object);
		return key;
	}

	@Override
	@Transactional
	public int insertAction(FileInfo object) {
		Date now = new Date();
		object.setCreateDate(now);
		object.setModifyDate(now);
		object.setDeleteFlag(0);
		int key = fileInfoDao.insertAction(object);
		return key;
	}

	@Override
	@Transactional
	public int insertAction(ReservationUserComment reservationUserComment, FileInfo fileInfo) {
		ReservationUserCommentImage reservationUserCommentImage = new ReservationUserCommentImage();
		Date now = new Date();
		reservationUserComment.setCreateDate(now);
		reservationUserComment.setModifyDate(now);

		int reservationUserCommentId = insertAction(reservationUserComment);

		fileInfo.setCreateDate(now);
		fileInfo.setModifyDate(now);

		int reservationInfoId = reservationUserComment.getReservationInfoId();

		fileInfo.setDeleteFlag(0);

		int fileInfoId = insertAction(fileInfo);

		reservationUserCommentImage.setReservationInfoId(reservationInfoId);
		reservationUserCommentImage.setReservationUserCommentId(reservationUserCommentId);
		reservationUserCommentImage.setFileId(fileInfoId);

		insertAction(reservationUserCommentImage);

		return reservationUserCommentId;
	}

	@Transactional
	@Override
	public int insertAction(ReservationUserComment reservationUserComment, List<FileInfo> fileInfos) {
		ReservationUserCommentImage reservationUserCommentImage = new ReservationUserCommentImage();
		Date now = new Date();
		reservationUserComment.setCreateDate(now);
		reservationUserComment.setModifyDate(now);
		int reservationInfoId = reservationUserComment.getReservationInfoId();
		int reservationUserCommentId = insertAction(reservationUserComment);

		fileInfos.forEach((v) -> {
			v.setCreateDate(now);
			v.setModifyDate(now);
			v.setDeleteFlag(0);
			int fileInfoId = insertAction(v);
			reservationUserCommentImage.setReservationInfoId(reservationInfoId);
			reservationUserCommentImage.setReservationUserCommentId(reservationUserCommentId);
			reservationUserCommentImage.setFileId(fileInfoId);
			insertAction(reservationUserCommentImage);

		});

		return reservationUserCommentId;
	}

	@Transactional
	@Override
	public Map<String, Object> writeReview(ReservationUserComment reservationUserComment, MultipartFile[] files) {
		int reservationUserCommentId;
		Map<String, Object> result = new HashMap<String, Object>();

		int filesLength = 0;
		if (files != null) {
			filesLength = files.length;
		} else {
			filesLength = 0;
		}

		if (filesLength > 1) {
			List<FileInfo> list = new ArrayList<>();
			for (MultipartFile file : files) {
				UUID uuid = UUID.randomUUID();
				FileInfo fileInfo = new FileInfo();
				String fileName = uuid + file.getOriginalFilename();
				String saveFileName = "review_img/" + uuid + file.getOriginalFilename();
				String contentType = file.getContentType();
				fileInfo.setFileName(fileName);
				fileInfo.setSaveFileName(saveFileName);
				fileInfo.setContentType(contentType);
				list.add(fileInfo);

				try (FileOutputStream fos = new FileOutputStream("/tmp/review_img/" + fileName);
						InputStream is = file.getInputStream();) {
					int readCount = 0;
					byte[] buffer = new byte[1024];
					while ((readCount = is.read(buffer)) != -1) {
						fos.write(buffer, 0, readCount);
					}

				} catch (Exception e) {
					throw new RuntimeException("file Save Error");
				}

			}
			reservationUserCommentId = insertAction(reservationUserComment, list);
			result.put("review", detailPageService.getReviewByReservationCommentId(reservationUserCommentId));
			return result;

		} else if (filesLength == 1) {
			UUID uuid = UUID.randomUUID();
			FileInfo fileInfo = new FileInfo();
			MultipartFile file = files[0];
			String fileName = uuid + file.getOriginalFilename();
			String saveFileName = "review_img/" + uuid + files[0].getOriginalFilename();
			String contentType = file.getContentType();
			fileInfo.setFileName(fileName);
			fileInfo.setSaveFileName(saveFileName);
			fileInfo.setContentType(contentType);

			try (FileOutputStream fos = new FileOutputStream("/tmp/review_img/" + fileName);
					InputStream is = file.getInputStream();) {
				int readCount = 0;
				byte[] buffer = new byte[1024];
				while ((readCount = is.read(buffer)) != -1) {
					fos.write(buffer, 0, readCount);
				}

			} catch (Exception e) {
				throw new RuntimeException("file Save Error");
			}

			reservationUserCommentId = insertAction(reservationUserComment, fileInfo);

			result.put("review", detailPageService.getReviewByReservationCommentId(reservationUserCommentId));
			return result;
		}

		reservationUserCommentId = insertAction(reservationUserComment);
		result.put("review", detailPageService.getReviewByReservationCommentId(reservationUserCommentId));
		return result;

	}

}
