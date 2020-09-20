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

	public void add(final ViewDomain viewDomain){
		try{
			LOGGER.debug("m=add(viewDomain={})", viewDomain);

			viewRepository.save(viewDomain);
		}catch (Exception exception){
			LOGGER.error("ex(message={}, cause={})", exception.getMessage(), exception);
			throw new RuntimeException(String.format("Falha ao processar a informação de visualização para o produto=%s da categoria=%",
					viewDomain.getItem().getProductId(),
					viewDomain.getItem().getCategoryId()));
		}
	}
}