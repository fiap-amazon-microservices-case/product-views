package br.com.fiap.aoj.productviews.configurations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.util.MimeType;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
class MappingJackson2MessageConverterConfiguration {

	@Bean
	@Primary
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper());

		return mappingJackson2HttpMessageConverter;
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		return  new ObjectMapper() //
				.registerModule(new Jdk8Module()) //
				.registerModule(new JavaTimeModule()) //
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) //
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true) //
				.setSerializationInclusion(JsonInclude.Include.ALWAYS);
	}

	@Bean
	public MappingJackson2MessageConverter mappingJackson2MessageConverter(
			final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
		final MimeType mimeTypeSupported = new MimeType("application", "json", UTF_8);
		final MappingJackson2MessageConverter mappingJackson2MessageConverter =
				new MappingJackson2MessageConverter(mimeTypeSupported);
		mappingJackson2MessageConverter.setObjectMapper(mappingJackson2HttpMessageConverter.getObjectMapper());

		return mappingJackson2MessageConverter;
	}
}