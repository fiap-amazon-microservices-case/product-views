package br.com.fiap.aoj.productviews.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "br.com.fiap.aoj.productviews.data")
class MongoConfiguration extends AbstractMongoClientConfiguration {

	private final String databaseName;

	public MongoConfiguration(@Value("${spring.data.mongodb.database:product-views}") final String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}
}
