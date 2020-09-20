package br.com.fiap.aoj.productviews.applications;

import br.com.fiap.aoj.productviews.data.ViewRepository;
import br.com.fiap.aoj.productviews.domain.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

@Service
public class GetViewsUseCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetViewsUseCase.class);

	private final MongoOperations mongoOperations;

	public GetViewsUseCase(final MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public Map<String, Set<Map.Entry<String, Integer>>> getGrouped(){
		LOGGER.debug("m=getGrouped()");

		final AggregationResults aggregationResults = getAggregation();
		final Map<String, Set<Map.Entry<String, Integer>>> grouped = new HashMap<>();
		aggregationResults.getMappedResults().stream() //
			.forEach(mr -> {
				final GroupViews groupViews = (GroupViews) mr;
				append(groupViews, grouped);
			});

		return grouped;
	}

	private void append(final GroupViews groupViews, final Map<String, Set<Map.Entry<String, Integer>>> grouped) {
		if(grouped.containsKey(groupViews.id.getCategoryId())){
			final Set<Map.Entry<String, Integer>> groupedCategoryFound = grouped.get(groupViews.id.getCategoryId());
			if(groupedCategoryFound.stream().anyMatch(entry -> entry.getKey().equalsIgnoreCase(groupViews.id.getProductId()))){
				final Map.Entry<String, Integer> stringIntegerEntry = groupedCategoryFound.stream()
						.filter(entry -> entry.getKey().equalsIgnoreCase(groupViews.id.getProductId()))
						.findFirst().get();
				stringIntegerEntry.setValue(stringIntegerEntry.getValue() + groupViews.count);

				groupedCategoryFound.add(stringIntegerEntry);
				return;
			}

			final Map.Entry<String, Integer> groupedProduct = new AbstractMap.SimpleEntry<>(groupViews.id.getProductId(), groupViews.count);
			groupedCategoryFound.add(groupedProduct);
			grouped.put(groupViews.id.getCategoryId(), groupedCategoryFound);
			return;
		}
		final Map.Entry<String, Integer> groupedProduct = new AbstractMap.SimpleEntry<>(groupViews.id.getProductId(),
				groupViews.count);
		grouped.put(groupViews.id.getCategoryId(), Stream.of(groupedProduct).collect(Collectors.toSet()));
	}

	private AggregationResults getAggregation() {
		final GroupOperation groupOperation = buildGroupOperation();
		SortOperation sortByPopDesc = sort(Sort.by(Sort.Direction.DESC, "count"));
		final Aggregation aggregation = Aggregation.newAggregation(groupOperation, sortByPopDesc);

		return mongoOperations.aggregate(aggregation, "viewDomain", GroupViews.class);
	}

	private GroupOperation buildGroupOperation() {
		return group("item.categoryId", "item.productId").sum("item.size").as("count");
	}

	static class GroupViews implements Serializable {

		private static final long serialVersionUID = 1223314928804115366L;

		@Id
		public Item id;

		public Integer count;
	}
}
