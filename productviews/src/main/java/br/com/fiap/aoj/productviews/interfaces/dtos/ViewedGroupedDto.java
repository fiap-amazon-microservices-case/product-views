package br.com.fiap.aoj.productviews.interfaces.dtos;

import java.util.HashSet;
import java.util.Set;

public class ViewedGroupedDto {

	private String categoryId;

	private Set<ViewedProductDto> viewedProducts = new HashSet<>();

	public String getCategoryId() {
		return categoryId;
	}

	public Set<ViewedProductDto> getViewedProducts() {
		return viewedProducts;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setViewedProducts(Set<ViewedProductDto> viewedProducts) {
		this.viewedProducts = viewedProducts;
	}
}
