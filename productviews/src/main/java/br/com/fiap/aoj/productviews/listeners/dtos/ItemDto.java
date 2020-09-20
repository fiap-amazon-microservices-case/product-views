package br.com.fiap.aoj.productviews.listeners.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDto {

	private String productId;
	private String categoryId;

	public String getProductId() {
		return productId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}
