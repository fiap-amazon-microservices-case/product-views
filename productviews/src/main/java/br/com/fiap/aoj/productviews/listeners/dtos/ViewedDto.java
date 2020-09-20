package br.com.fiap.aoj.productviews.listeners.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ViewedDto {

	private UUID id;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime viewedAt;

	private ItemDto item;

	public UUID getId() {
		return id;
	}

	public LocalDateTime getViewedAt() {
		return viewedAt;
	}

	public ItemDto getItem() {
		return item;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setViewedAt(LocalDateTime viewedAt) {
		this.viewedAt = viewedAt;
	}

	public void setItem(ItemDto item) {
		this.item = item;
	}
}