package br.com.fiap.aoj.productviews.listeners;

import br.com.fiap.aoj.productviews.applications.AddViewUseCase;
import br.com.fiap.aoj.productviews.domain.Item;
import br.com.fiap.aoj.productviews.domain.ViewDomain;
import br.com.fiap.aoj.productviews.listeners.dtos.ItemDto;
import br.com.fiap.aoj.productviews.listeners.dtos.ViewedDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
class ViewedEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewedEventListener.class);

	private final AddViewUseCase addViewUseCase;

	ViewedEventListener(final AddViewUseCase addViewUseCase) {
		this.addViewUseCase = addViewUseCase;
	}

	@SqsListener(value = "${aws.sqs.viewed-product}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void viewedEventListener(ViewedDto viewedDto){
		LOGGER.info("m=viewedEventListener(viewedDto={})", viewedDto);

		final ItemDto itemDto = viewedDto.getItem();
		if(isNull(itemDto.getProductId()) || isNull(itemDto.getCategoryId())){
			LOGGER.warn("Dados não informados, ID do produto={} ou ID da categoria={}", itemDto.getProductId(), itemDto.getCategoryId());
			return;
		}

		final ViewDomain viewDomain = build(viewedDto);
		addViewUseCase.add(viewDomain);
	}

	private ViewDomain build(final ViewedDto viewedDto) {
		String id = UUID.randomUUID().toString();
		LocalDateTime viewedAt = viewedDto.getViewedAt();
		if(nonNull(viewedDto.getId())){
			id = viewedDto.getId().toString();
		}
		if(nonNull(viewedDto.getViewedAt())){
			viewedAt = viewedDto.getViewedAt();
		}

		final ItemDto itemDto = viewedDto.getItem();
		return build(id, viewedAt, itemDto.getProductId(), itemDto.getCategoryId());
	}

	private ViewDomain build(final String id, final LocalDateTime viewedAt, final String productId, final String categoryId) {
		final Item item = Item.of(productId, categoryId);
		return ViewDomain.builder() //
				.item(item) //
				.id(id) //
				.viewedAt(viewedAt) //
				.builder();
	}
}