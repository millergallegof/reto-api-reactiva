package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class CheckAvailabilityUseCaseTest {
    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    CheckAvailabilityUseCase checkAvailabilityUseCase;

    @Test
    void apply() {
        Resource resourceTwo = new Resource();
        resourceTwo.setId("987456aa");
        resourceTwo.setName("Nombre #2");
        resourceTwo.setType("Tipo #3");
        resourceTwo.setCategory("admon");
        resourceTwo.setUnitsAvailable(8);
        resourceTwo.setUnitsOwed(6);
        resourceTwo.setLastBorrow(LocalDate.parse("2023-10-29"));

        Resource resourceOne = new Resource();
        resourceOne.setId("123456ff");
        resourceOne.setName("Nombre #1");
        resourceOne.setType("Tipo #1");
        resourceOne.setCategory("produccion");
        resourceOne.setUnitsAvailable(0);
        resourceOne.setUnitsOwed(7);
        resourceOne.setLastBorrow(LocalDate.parse("2023-11-01"));

        Mockito.when(resourceRepository.findById(any(String.class)))
                .thenReturn(Mono.just(resourceTwo));

        StepVerifier
                .create(checkAvailabilityUseCase.apply("987456aa"))
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res, "Nombre #2" + "is available");
                })
                .expectComplete()
                .verify();

        Mockito.when(resourceRepository.findById(any(String.class)))
                .thenReturn(Mono.just(resourceOne));

        StepVerifier
                .create(checkAvailabilityUseCase.apply("123456ff"))
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res, "Nombre #1" + "is not available, last borrow " + "2023-11-01");
                })
                .expectComplete()
                .verify();

    }

}