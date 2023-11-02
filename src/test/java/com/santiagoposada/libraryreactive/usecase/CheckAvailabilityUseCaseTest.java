package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;

class CheckAvailabilityUseCaseTest {
    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    CheckAvailabilityUseCase checkAvailabilityUseCase;

    @Test
    void apply() {


    }
}