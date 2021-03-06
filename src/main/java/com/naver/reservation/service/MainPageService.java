package com.naver.reservation.service;

import java.util.List;

import com.naver.reservation.main.dto.Category;
import com.naver.reservation.main.dto.MainProduct;

public interface MainPageService {
	public List<MainProduct> getPromotionInfo(String type);
	public List<MainProduct> getProductByCategoryId(int id, int start);
	public List<MainProduct> getProductAll(int start);
	public List<MainProduct> getAllProductByCategoryId(int id);
	public Integer selectCount();
	public Integer selectCountByCategoryId(int id);
	public List<Category> getCategories();
}
