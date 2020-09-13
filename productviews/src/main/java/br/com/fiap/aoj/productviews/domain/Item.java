package br.com.fiap.aoj.productviews.domain;

import java.io.Serializable;

public class Item implements Serializable {
	private static final int defaultSize = 1;
	private static final long serialVersionUID = -778324349703125031L;

	private String productId;

	private String categoryId;

	private int size = defaultSize;

	private Item(final String productId, final String categoryId){
		this.productId = productId;
		this.categoryId = categoryId;
	}

	//Construtor padrão para serialização do mongo
	public Item(){}

	public String getProductId() {
		return productId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public static final Item of(final String productId, final String categoryId){
		return new Item(productId, categoryId);
	}
}