package com.caep.franchise_handler_ce.application.service;

import com.caep.franchise_handler_ce.domain.dto.ProductWithBranchDto;
import com.caep.franchise_handler_ce.domain.model.Branch;
import com.caep.franchise_handler_ce.domain.model.Franchise;
import com.caep.franchise_handler_ce.domain.model.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseService {

    Mono<Franchise> createFranchise(Franchise franchise);

    Mono<Branch> addBranch(String franchiseId, Branch branch);

    Mono<Product> addProduct(String franchiseId, String branchId, Product product);

    Mono<Void> deleteProduct(String franchiseId, String branchId, String productId);

    Mono<Product> updateProductStock(String franchiseId, String branchId, String productId, int newStock);

    Flux<ProductWithBranchDto> topProductsByFranchise(String franchiseId);

    Mono<Franchise> updateFranchiseName(String franchiseId, String newName);

    Mono<Branch> updateBranchName(String franchiseId, String branchId, String newName);

    Mono<Product> updateProductName(String franchiseId, String branchId, String productId, String newName);

    //Gets for testing purposes
    Flux<Franchise> getAllFranchises();

    Mono<Franchise> getFranchiseById(String franchiseId);

    Flux<Branch> getBranchesByFranchise(String franchiseId);

    Flux<Product> getProductsByBranch(String franchiseId, String branchId);
}
