package br.com.fiap.aoj.productviews.interfaces.dtos;

public class ViewedProductDto {

	private String productId;

	private Integer views;

	public String getProductId() {
		return productId;
	}

	public Integer getViews() {
		return views;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setViews(Integer views) {
		this.views = views;
	}
}