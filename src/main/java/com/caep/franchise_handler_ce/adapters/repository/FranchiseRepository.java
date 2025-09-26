package com.caep.franchise_handler_ce.adapters.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.caep.franchise_handler_ce.domain.model.Franchise;

@Repository
public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {
}
