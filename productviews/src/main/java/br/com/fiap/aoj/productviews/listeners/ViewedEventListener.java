package br.com.fiap.aoj.productviews.listeners;

import br.com.fiap.aoj.productviews.applications.AddViewUseCase;
import br.com.fiap.aoj.productviews.listeners.dtos.ViewedDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
class ViewedEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewedEventListener.class);

	private final AddViewUseCase addViewUseCase;

	ViewedEventListener(final AddViewUseCase addViewUseCase) {
		this.addViewUseCase = addViewUseCase;
	}

	//TODO: Implementar o listener do tópico
	public void viewedEventListener(final ViewedDto viewedDto){
		LOGGER.info("m=viewedEventListener(viewedDto={})", viewedDto);

		final String productId = viewedDto.getProductId();
		final String categoryId = viewedDto.getCategoryId();
		if(isNull(productId) || isNull(categoryId)){
			LOGGER.warn("Dados não informados, ID do produto={} ou ID da categoria={}", productId, categoryId);
			return;
		}

		addViewUseCase.add(productId, categoryId);
	}
}