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
class BorrowResourceUseCaseTest {
    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    BorrowResourceUseCase borrowResourceUseCase;

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

        Resource resourceUpdate= new Resource();
        resourceUpdate.setId("987456aa");
        resourceUpdate.setName("Nombre #2");
        resourceUpdate.setType("Tipo #3");
        resourceUpdate.setCategory("admon");
        resourceUpdate.setUnitsAvailable(7);
        resourceUpdate.setUnitsOwed(7);
        resourceUpdate.setLastBorrow(LocalDate.parse("2023-10-29"));

        Mockito.when(resourceRepository.findById(any(String.class)))
                .thenReturn(Mono.just(resourceTwo));
        Mockito.when(resourceRepository.save(any(Resource.class)))
                .thenReturn(Mono.just(resourceUpdate));

        StepVerifier
                .create(borrowResourceUseCase.apply("987456aa"))
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res, "The resource "
                            + "Nombre #2" + " has been borrowed, there are "
                            + "7" + " units available");
                })
                .expectComplete()
                .verify();
    }
}