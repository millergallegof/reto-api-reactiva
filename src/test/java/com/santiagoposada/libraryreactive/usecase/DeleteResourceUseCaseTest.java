package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
@SpringBootTest
class DeleteResourceUseCaseTest {
    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    DeleteResourceUseCase deleteResourceUseCase;

    @Test
    void apply() {
        Mockito.when(resourceRepository.deleteById(any(String.class)))
                .thenReturn(Mono.empty());

        StepVerifier
                .create(deleteResourceUseCase.apply("1233435ff"))
                .expectComplete()
                .verify();

        Mockito.verify(resourceRepository, Mockito.times(1)).deleteById("1233435ff");

    }
}