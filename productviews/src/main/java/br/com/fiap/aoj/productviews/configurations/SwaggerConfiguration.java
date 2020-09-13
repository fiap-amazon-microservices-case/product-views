package br.com.fiap.aoj.productviews.configurations;

import org.springframework.context.annotation.Configuration;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static br.com.fiap.aoj.productviews.Application.BASE_PACKAGE;

@Configuration
@EnableSwagger2
class SwaggerConfiguration implements WebFluxConfigurer {

	@Value("${api.version.v1:/v1}")
	private String apiVersion;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.genericModelSubstitutes(Mono.class, Flux.class, Publisher.class)
				.select()
				.apis(RequestHandlerSelectors.basePackage(buildBasePackage()))
				.paths(PathSelectors.ant(buildPath()))
				.build();
	}

	private String buildPath() {
		return apiVersion + "/**";
	}

	private String buildBasePackage() {
		return BASE_PACKAGE + ".interfaces";
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler( "/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
				.resourceChain(false);
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}