package com.caep.franchise_handler_ce.adapters.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.caep.franchise_handler_ce.application.service.FranchiseService;
import com.caep.franchise_handler_ce.domain.dto.NameUpdateRequest;
import com.caep.franchise_handler_ce.domain.dto.ProductWithBranchDto;
import com.caep.franchise_handler_ce.domain.dto.StockUpdateRequest;
import com.caep.franchise_handler_ce.domain.model.Branch;
import com.caep.franchise_handler_ce.domain.model.Franchise;
import com.caep.franchise_handler_ce.domain.model.Product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> createFranchise(@Valid @RequestBody Franchise franchise) {
        return service.createFranchise(franchise);
    }

    @PostMapping("/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Branch> addBranch(@PathVariable String franchiseId, @Valid @RequestBody Branch branch) {
        return service.addBranch(franchiseId, branch);
    }

    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> addProduct(@PathVariable String franchiseId,
            @PathVariable String branchId,
            @Valid @RequestBody Product product) {
        return service.addProduct(franchiseId, branchId, product);
    }

    @DeleteMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId) {
        return service.deleteProduct(franchiseId, branchId, productId);
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    public Mono<Product> updateStock(@PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestBody(required = true) StockUpdateRequest request) {
        return service.updateProductStock(franchiseId, branchId, productId, request.getStock());
    }

    @GetMapping("/{franchiseId}/top-products")
    public Flux<ProductWithBranchDto> topProducts(@PathVariable String franchiseId) {
        return service.topProductsByFranchise(franchiseId);
    }

    // Plus endpoints: update names
    @PatchMapping("/{franchiseId}")
    public Mono<Franchise> updateFranchiseName(@PathVariable String franchiseId,
            @RequestBody NameUpdateRequest req) {
        return service.updateFranchiseName(franchiseId, req.getName());
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}")
    public Mono<Branch> updateBranchName(@PathVariable String franchiseId,
            @PathVariable String branchId,
            @RequestBody NameUpdateRequest req) {
        return service.updateBranchName(franchiseId, branchId, req.getName());
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    public Mono<Product> updateProductName(@PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestBody NameUpdateRequest req) {
        return service.updateProductName(franchiseId, branchId, productId, req.getName());
    }

    //Gets for testing purposes
    @GetMapping
    public Flux<Franchise> getAllFranchises() {
        return service.getAllFranchises();
    }

    @GetMapping("/{franchiseId}")
    public Mono<Franchise> getFranchiseById(@PathVariable String franchiseId) {
        return service.getFranchiseById(franchiseId);
    }

    @GetMapping("/{franchiseId}/branches")
    public Flux<Branch> getBranches(@PathVariable String franchiseId) {
        return service.getBranchesByFranchise(franchiseId);
    }

    @GetMapping("/{franchiseId}/branches/{branchId}/products")
    public Flux<Product> getProducts(@PathVariable String franchiseId, @PathVariable String branchId) {
        return service.getProductsByBranch(franchiseId, branchId);
    }
}
