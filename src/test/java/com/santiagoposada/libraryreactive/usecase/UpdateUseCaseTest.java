package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static reactor.core.publisher.Mono.when;
@SpringBootTest
class UpdateUseCaseTest {
    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    UpdateUseCase updateUseCase;
    @SpyBean
    ResourceMapper resourceMapper;

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

        Mockito.when(resourceRepository.save(any(Resource.class)))
                .thenReturn(Mono.just(resourceTwo));

        StepVerifier
                .create(updateUseCase.apply(resourceMapper.fromResourceEntityToDTO().apply(resourceTwo)))
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res.getName(), "Nombre #2");
                    assertEquals(res.getCategory(), "admon");
                })
                .expectComplete()
                .verify();
    }
}