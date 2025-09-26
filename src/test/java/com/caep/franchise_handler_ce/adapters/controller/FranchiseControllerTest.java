package com.caep.franchise_handler_ce.adapters.controller;

import com.caep.franchise_handler_ce.application.service.FranchiseService;
import com.caep.franchise_handler_ce.domain.model.Franchise;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = FranchiseController.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private FranchiseService service;

    @Test
    void createFranchise_shouldReturnCreated() {
        Franchise f = Franchise.builder().id("1").name("Test").build();
        when(service.createFranchise(f)).thenReturn(Mono.just(f));

        webClient.post()
                .uri("/api/franchises")
                .bodyValue(f)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.name").isEqualTo("Test");
    }
}
