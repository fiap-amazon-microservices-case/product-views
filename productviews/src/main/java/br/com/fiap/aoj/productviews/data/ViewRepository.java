package br.com.fiap.aoj.productviews.data;

import br.com.fiap.aoj.productviews.domain.ViewDomain;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ViewRepository extends MongoRepository<ViewDomain, UUID> { }