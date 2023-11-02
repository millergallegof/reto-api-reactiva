package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class GetByTypeUseCaseTest {

    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    GetByTypeUseCase getByTypeUseCase;


    @Test
    void apply() {
        List<Resource> listResource = new ArrayList<>();
        Resource resource = new Resource();
        resource.setId("1233435ff");
        resource.setName("Nombre #1");
        resource.setType("Tipo #1");
        resource.setCategory("Area tematica #1");
        resource.setUnitsAvailable(10);
        resource.setUnitsOwed(5);
        resource.setLastBorrow(LocalDate.parse("2020-01-10"));

        Resource resourceTwo = new Resource();
        resourceTwo.setId("987456aa");
        resourceTwo.setName("Nombre #2");
        resourceTwo.setType("Tipo #3");
        resourceTwo.setCategory("admon");
        resourceTwo.setUnitsAvailable(8);
        resourceTwo.setUnitsOwed(6);
        resourceTwo.setLastBorrow(LocalDate.parse("2023-10-29"));

        listResource.add(resource);
//        listResource.add(resourceTwo);

        Mockito.when(resourceRepository.findAllByType(any(String.class)))
                .thenReturn(Flux.fromIterable(listResource));

        StepVerifier
                .create(getByTypeUseCase.apply("Tipo #1"))
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res.getId(), "1233435ff");
                })
                .expectComplete()
                .verify();
    }
}