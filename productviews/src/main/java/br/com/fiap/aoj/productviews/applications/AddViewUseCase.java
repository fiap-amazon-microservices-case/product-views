package br.com.fiap.aoj.productviews.applications;

import br.com.fiap.aoj.productviews.data.ViewRepository;
import br.com.fiap.aoj.productviews.domain.Item;
import br.com.fiap.aoj.productviews.domain.ViewDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AddViewUseCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddViewUseCase.class);

	private final ViewRepository viewRepository;

	public AddViewUseCase(final ViewRepository viewRepository) {
		this.viewRepository = viewRepository;
	}

	public void add(final String productId, final String categoryId){
		try{
			LOGGER.debug("m=add(productId={}, categoryId={})", productId, categoryId);

			final ViewDomain viewDomain = build(productId, categoryId);
			viewRepository.save(viewDomain);
		}catch (Exception exception){
			LOGGER.error("ex(message={}, cause={})", exception.getMessage(), exception);
		}
	}

	private ViewDomain build(final String productId, final String categoryId) {
		final Item item = Item.of(productId, categoryId);
		return ViewDomain.builder() //
					.item(item) //
					.builder();
	}
}