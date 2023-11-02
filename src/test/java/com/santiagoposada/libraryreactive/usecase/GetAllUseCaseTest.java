package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.dto.ResourceDTO;
import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetAllUseCaseTest {
    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    GetAllUseCase getAllUseCase;

    @Test
    void get() {
        List<Resource> listResource = new ArrayList<>();
        Resource resource = new Resource();
        resource.setId("1233435ff");
        resource.setName("Nombre #1");
        resource.setType("Tipo #1");
        resource.setCategory("Area tematica #1");
        resource.setUnitsAvailable(10);
        resource.setUnitsOwed(5);
        resource.setLastBorrow(LocalDate.parse("2020-01-10"));

        listResource.add(resource);

        Mockito.when(resourceRepository.findAll()).thenReturn(Flux.fromIterable(listResource));

        StepVerifier
                .create(getAllUseCase.get())
                .consumeNextWith(res -> {
                    assertNotNull(res.getId());
                    assertEquals(res.getId(), "1233435ff");
                    assertEquals(res.getName(), "Nombre #1");
                    assertEquals(res.getType(), "Tipo #1");
                    assertEquals(res.getCategory(), "Area tematica #1");
                    assertEquals(res.getUnitsAvailable(), 10);
                    assertEquals(res.getUnitsOwed(), 5);
                })
                .verifyComplete();
    }
}