package br.com.fiap.aoj.productviews.interfaces;


import br.com.fiap.aoj.productviews.applications.GetViewsUseCase;
import br.com.fiap.aoj.productviews.interfaces.dtos.ViewedGroupedDto;
import br.com.fiap.aoj.productviews.interfaces.dtos.ViewedProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "${api.version.v1:/v1}/views")
class ProductViewsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewsController.class);

	private final GetViewsUseCase getViewsUseCase;

	ProductViewsController(final GetViewsUseCase getViewsUseCase) {
		this.getViewsUseCase = getViewsUseCase;
	}

	@GetMapping
	@ResponseStatus(OK)
	public Flux<Stream<ViewedGroupedDto>> find(){
		LOGGER.debug("m=find()");

		final Map<String, Set<Map.Entry<String, Integer>>> grouped = getViewsUseCase.getGrouped();
		return Flux.just(buildViewedGrouped(grouped));
	}

	private Stream<ViewedGroupedDto> buildViewedGrouped(final Map<String, Set<Map.Entry<String, Integer>>> grouped) {
		return grouped.entrySet().stream() //
					.map(entry -> {
						final ViewedGroupedDto viewedGroupedDto = new ViewedGroupedDto();
						viewedGroupedDto.setCategoryId(entry.getKey());
						viewedGroupedDto.setViewedProducts(buildViewedProducts(entry.getValue()));

						return viewedGroupedDto;
					});
	}

	private Set<ViewedProductDto> buildViewedProducts(final Set<Map.Entry<String, Integer>> grouped) {
		return grouped.stream().map(entry -> {
			final ViewedProductDto viewedProductDto = new ViewedProductDto();
			viewedProductDto.setProductId(entry.getKey());
			viewedProductDto.setViews(entry.getValue());
			return viewedProductDto;
		}).collect(Collectors.toSet());
	}
}