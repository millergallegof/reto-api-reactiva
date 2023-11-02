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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GetResourceByIdUseCaseTest {
    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    GetResourceByIdUseCase getResourceByIdUseCase;

    @Test
    void apply() {
        Resource resource = new Resource();
        resource.setId("1233435ff");
        resource.setName("Nombre #1");
        resource.setType("Tipo #1");
        resource.setCategory("Area tematica #1");
        resource.setUnitsAvailable(10);
        resource.setUnitsOwed(5);
        resource.setLastBorrow(LocalDate.parse("2020-01-10"));

        Mockito.when(resourceRepository.findById(Mockito.any(String.class)))
                .thenReturn(Mono.just(resource));

        StepVerifier
                .create(getResourceByIdUseCase.apply("1233435ff"))
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res.getName(), "Nombre #1");
                    assertEquals(res.getUnitsOwed(), 5);
                    assertEquals(res.getCategory(), "Area tematica #1");
                })
                .expectComplete()
                .verify();
    }
}