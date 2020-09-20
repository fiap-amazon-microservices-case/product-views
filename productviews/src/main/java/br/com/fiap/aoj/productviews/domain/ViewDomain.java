package br.com.fiap.aoj.productviews.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class ViewDomain implements Serializable {

	private static final long serialVersionUID = 1649219665299117651L;

	//TODO: Já receber o ID e Data do view
	@MongoId
	private UUID id;

	@Indexed
	private br.com.fiap.aoj.productviews.domain.Item item;

	private LocalDateTime viewedAt;

	private Integer productsCount;

	private ViewDomain(final Builder builder){
		this.id = builder.id;
		this.item = builder.item;
		this.viewedAt = builder.viewedAt;
	}

	//Construtor padrão para serialização do mongo
	public ViewDomain(){}

	public UUID getId() {
		return id;
	}

	public br.com.fiap.aoj.productviews.domain.Item getItem() {
		return item;
	}

	public LocalDateTime viewedAt() {
		return viewedAt;
	}

	public Integer getProductsCount() {
		return productsCount;
	}

	public static final Item builder(){
		return new Builder();
	}

	public static final class Builder implements Item, Build{
		private UUID id = UUID.randomUUID();
		private br.com.fiap.aoj.productviews.domain.Item item;
		private LocalDateTime viewedAt = LocalDateTime.now();

		@Override
		public Build item(final br.com.fiap.aoj.productviews.domain.Item item) {
			this.item = item;
			return this;
		}

		@Override
		public Build id(final String id) {
			this.id = UUID.fromString(id);
			return this;
		}

		@Override
		public Build viewedAt(final LocalDateTime viewedAt) {
			this.viewedAt = viewedAt;
			return this;
		}

		@Override
		public ViewDomain builder() {
			return new ViewDomain(this);
		}
	}

	public interface Item {
		public Build item(final br.com.fiap.aoj.productviews.domain.Item item);
	}

	public interface Build{
		public Build id(final String id);
		public Build viewedAt(final LocalDateTime viewedAt);
		public ViewDomain builder();
	}
}
