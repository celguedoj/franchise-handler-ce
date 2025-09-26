package com.caep.franchise_handler_ce.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.caep.franchise_handler_ce.adapters.repository.FranchiseRepository;
import com.caep.franchise_handler_ce.domain.model.Branch;
import com.caep.franchise_handler_ce.domain.model.Franchise;
import com.caep.franchise_handler_ce.domain.model.Product;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReactiveFranchiseServiceTest {

    private FranchiseRepository repository;
    private ReactiveFranchiseService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(FranchiseRepository.class);
        service = new ReactiveFranchiseService(repository);
    }

    @Test
    void createFranchise_shouldSaveAndReturnFranchise() {
        Franchise franchise = Franchise.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Franchise")
                .build();

        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.createFranchise(franchise))
                .expectNextMatches(f -> f.getName().equals("Test Franchise"))
                .verifyComplete();

        verify(repository, times(1)).save(any(Franchise.class));
    }

    @Test
    void addBranch_shouldAddBranchToFranchise() {
        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Test Franchise")
                .branches(new ArrayList<>()) // ✅ lista mutable
                .build();

        Branch newBranch = Branch.builder().id("b1").name("Sucursal 1").build();

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.addBranch("1", newBranch))
                .expectNextMatches(b -> b.getName().equals("Sucursal 1"))
                .verifyComplete();

        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).save(any(Franchise.class));
    }

    @Test
    void updateProductStock_shouldUpdateStockWhenExists() {
        Product product = Product.builder().id("p1").name("Prod 1").stock(10).build();
        Branch branch = Branch.builder()
                .id("b1")
                .name("Sucursal 1")
                .products(new ArrayList<>(List.of(product))) // ✅ mutable
                .build();
        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Franchise 1")
                .branches(new ArrayList<>(List.of(branch))) // ✅ mutable
                .build();

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.updateProductStock("1", "b1", "p1", 99))
                .expectNextMatches(p -> p.getStock() == 99)
                .verifyComplete();

        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).save(any(Franchise.class));
    }

    @Test
    void updateProductStock_shouldErrorWhenProductNotFound() {
        Branch branch = Branch.builder()
                .id("b1")
                .name("Sucursal 1")
                .products(new ArrayList<>()) // ✅ mutable
                .build();
        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Franchise 1")
                .branches(new ArrayList<>(List.of(branch))) // ✅ mutable
                .build();

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.updateProductStock("1", "b1", "pX", 50))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException
                        && err.getMessage().equals("Product not found"))
                .verify();
    }
}
