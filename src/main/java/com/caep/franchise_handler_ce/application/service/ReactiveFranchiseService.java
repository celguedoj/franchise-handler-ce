package com.caep.franchise_handler_ce.application.service;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.caep.franchise_handler_ce.adapters.repository.FranchiseRepository;
import com.caep.franchise_handler_ce.domain.dto.ProductWithBranchDto;
import com.caep.franchise_handler_ce.domain.model.Branch;
import com.caep.franchise_handler_ce.domain.model.Franchise;
import com.caep.franchise_handler_ce.domain.model.Product;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveFranchiseService implements FranchiseService {

    private final FranchiseRepository repo;

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        if (franchise.getId() == null) {
            franchise.setId(UUID.randomUUID().toString());
        }
        return repo.save(franchise);
    }

    @Override
    public Mono<Branch> addBranch(String franchiseId, Branch branch) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> {
                    if (branch.getId() == null) {
                        branch.setId(UUID.randomUUID().toString());
                    }
                    if (fr.getBranches() == null) {
                        fr.setBranches(new java.util.ArrayList<>());
                    }
                    fr.getBranches().add(branch);
                    return repo.save(fr).map(saved -> branch);
                });
    }

    @Override
    public Mono<Product> addProduct(String franchiseId, String branchId, Product product) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> {
                    Optional<Branch> maybe = fr.getBranches().stream().filter(b -> branchId.equals(b.getId())).findFirst();
                    if (maybe.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Branch not found"));
                    }
                    Branch branch = maybe.get();
                    if (product.getId() == null) {
                        product.setId(UUID.randomUUID().toString());
                    }
                    if (branch.getProducts() == null) {
                        branch.setProducts(new java.util.ArrayList<>());
                    }
                    branch.getProducts().add(product);
                    return repo.save(fr).map(s -> product);
                });
    }

    @Override
    public Mono<Void> deleteProduct(String franchiseId, String branchId, String productId) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> {
                    Optional<Branch> maybe = fr.getBranches().stream().filter(b -> branchId.equals(b.getId())).findFirst();
                    if (maybe.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Branch not found"));
                    }
                    Branch branch = maybe.get();
                    if (branch.getProducts() != null) {
                        branch.getProducts().removeIf(p -> productId.equals(p.getId()));
                    }
                    return repo.save(fr).then();
                });
    }

    @Override
    public Mono<Product> updateProductStock(String franchiseId, String branchId, String productId, int newStock) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> {
                    Optional<Branch> maybeB = fr.getBranches().stream().filter(b -> branchId.equals(b.getId())).findFirst();
                    if (maybeB.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Branch not found"));
                    }
                    Branch branch = maybeB.get();
                    Optional<Product> maybeP = branch.getProducts().stream().filter(p -> productId.equals(p.getId())).findFirst();
                    if (maybeP.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Product not found"));
                    }
                    Product p = maybeP.get();
                    p.setStock(newStock);
                    return repo.save(fr).map(s -> p);
                });
    }

    @Override
    public Flux<ProductWithBranchDto> topProductsByFranchise(String franchiseId) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMapMany(fr -> {
                    return Flux.fromIterable(fr.getBranches())
                            .flatMap(branch -> {
                                if (branch.getProducts() == null || branch.getProducts().isEmpty()) {
                                    return Flux.empty();
                                }
                                Optional<Product> top = branch.getProducts().stream()
                                        .max(Comparator.comparingInt(Product::getStock));
                                return top.map(product -> Flux.just(
                                        ProductWithBranchDto.builder()
                                                .branchId(branch.getId())
                                                .branchName(branch.getName())
                                                .productId(product.getId())
                                                .productName(product.getName())
                                                .stock(product.getStock())
                                                .build()
                                )).orElse(Flux.empty());
                            });
                });
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> {
                    fr.setName(newName);
                    return repo.save(fr);
                });
    }

    @Override
    public Mono<Branch> updateBranchName(String franchiseId, String branchId, String newName) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> {
                    Optional<Branch> maybe = fr.getBranches().stream().filter(b -> branchId.equals(b.getId())).findFirst();
                    if (maybe.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Branch not found"));
                    }
                    Branch b = maybe.get();
                    b.setName(newName);
                    return repo.save(fr).map(s -> b);
                });
    }

    @Override
    public Mono<Product> updateProductName(String franchiseId, String branchId, String productId, String newName) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> {
                    Optional<Branch> maybeB = fr.getBranches().stream().filter(b -> branchId.equals(b.getId())).findFirst();
                    if (maybeB.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Branch not found"));
                    }
                    Branch branch = maybeB.get();
                    Optional<Product> maybeP = branch.getProducts().stream().filter(p -> productId.equals(p.getId())).findFirst();
                    if (maybeP.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Product not found"));
                    }
                    Product p = maybeP.get();
                    p.setName(newName);
                    return repo.save(fr).map(s -> p);
                });
    }

    //Gets for testing purposes
    @Override
    public Flux<Franchise> getAllFranchises() {
        return repo.findAll();
    }

    @Override
    public Mono<Franchise> getFranchiseById(String franchiseId) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")));
    }

    @Override
    public Flux<Branch> getBranchesByFranchise(String franchiseId) {
        return repo.findById(franchiseId)
                .flatMapMany(fr -> Flux.fromIterable(fr.getBranches()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found or no branches")));
    }

    @Override
    public Flux<Product> getProductsByBranch(String franchiseId, String branchId) {
        return repo.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMapMany(fr -> {
                    return fr.getBranches().stream()
                            .filter(b -> branchId.equals(b.getId()))
                            .findFirst()
                            .map(branch -> Flux.fromIterable(branch.getProducts()))
                            .orElse(Flux.error(new IllegalArgumentException("Branch not found")));
                });
    }
}
