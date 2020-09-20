package br.com.fiap.aoj.productviews.configurations;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

import static com.amazonaws.Protocol.HTTPS;
import static java.util.Collections.singletonList;


@Configuration
class AwsConfiguration {

	private final String region;
	private final String accessKey;
	private final String secretKey;

	public AwsConfiguration(@Value("${cloud.aws.region.static}") final String region,
			@Value("${cloud.aws.credentials.accessKey}") final String accessKey,
			@Value("${cloud.aws.credentials.secretKey}") final String secretKey) {
		this.region = region;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}
	@Bean
	@Primary
	public AmazonSQSAsync amazonSQSAsync() {
		return AmazonSQSAsyncClientBuilder
				.standard()
				.withCredentials(getAwsCredentials(accessKey, secretKey))
				.withRegion(getRegion(region))
				.withClientConfiguration(getClientConfiguration())
				.build();
	}

	private ClientConfiguration getClientConfiguration() {
		return new ClientConfiguration()
				.withProtocol(HTTPS);
	}

	private Regions getRegion(final String region) {
		return Regions.fromName(region);
	}

	private AWSCredentialsProvider getAwsCredentials(final String accessKey, final String secretKey) {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return new AWSStaticCredentialsProvider(basicAWSCredentials);
	}

	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(
			@Qualifier("amazonSQSAsync") final AmazonSQSAsync amazonSQSAsync,
			@Qualifier("threadPoolTaskExecutor") final TaskExecutor taskExecutor) {
		final SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory =
				new SimpleMessageListenerContainerFactory();
		simpleMessageListenerContainerFactory.setAmazonSqs(amazonSQSAsync);
		simpleMessageListenerContainerFactory.setTaskExecutor(getConcurrentTaskExecutor(taskExecutor));

		return simpleMessageListenerContainerFactory;
	}

	private AsyncTaskExecutor getConcurrentTaskExecutor(final Executor asyncExecutor) {
		return new ConcurrentTaskExecutor(asyncExecutor);
	}

	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory(
			@Qualifier("amazonSQSAsync") final AmazonSQSAsync amazonSQSAsync,
			@Qualifier("mappingJackson2MessageConverter") final MappingJackson2MessageConverter mappingJackson2MessageConverter) {
		final QueueMessageHandlerFactory queueMessageHandlerFactory = new QueueMessageHandlerFactory();
		queueMessageHandlerFactory.setArgumentResolvers(getPayloadArgumentResolver(mappingJackson2MessageConverter));
		queueMessageHandlerFactory.setSendToMessagingTemplate(new QueueMessagingTemplate(amazonSQSAsync));
		queueMessageHandlerFactory.setAmazonSqs(amazonSQSAsync);

		return queueMessageHandlerFactory;
	}

	private List<HandlerMethodArgumentResolver> getPayloadArgumentResolver(
			final MappingJackson2MessageConverter mappingJackson2MessageConverter) {
		return singletonList(new PayloadMethodArgumentResolver(mappingJackson2MessageConverter));
	}

}