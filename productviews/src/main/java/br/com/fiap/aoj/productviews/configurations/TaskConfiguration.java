package br.com.fiap.aoj.productviews.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
class TaskConfiguration {

	private final String applicationName;

	private final Integer corePoolSize;

	public TaskConfiguration( //
			@Value("${spring.application.name:product-views}") final String applicationName, //
			@Value("${application.core-pool-size:10}") final Integer corePoolSize) {
		this.applicationName = applicationName;
		this.corePoolSize = corePoolSize;
	}

	@Bean(name = "threadPoolTaskExecutor")
	public TaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(Integer.MAX_VALUE);
		executor.setQueueCapacity(Integer.MAX_VALUE);
		executor.setThreadNamePrefix(getName());
		executor.initialize();
		return executor;
	}

	private String getName() {
		final StringBuilder builder = new StringBuilder();
		builder //
				.append(applicationName) //
				.append("-");

		return builder.toString();
	}

}
