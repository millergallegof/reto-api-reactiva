package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.dto.ResourceDTO;
import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;


@SpringBootTest
class CreateResourceUseCaseTest {

//    la diferencia entre spybeans y mockbeans es que mockbeans mockea un objeto totalmente diferente
    @MockBean
    private ResourceRepository resourceRepository;

//    basicamente lo que hace el spybean es mockear el servicio, sin cambiar las funcionalidades que tiene en su interior
    @SpyBean
    private CreateResourceUseCase createResourceUseCase;

//    la anotacion BeforeEach -> indica que ese metodo se va a ejecutar cada que se corra algun test unitario
    @BeforeEach
    public void setup(){

    }


//    la etiqueta displayName funciona para darle un nombre especifico al test en cuestion
    @Test
    @DisplayName("Create resource")
    void createResourceTest() {
        // Arrange
        Resource resource = new Resource();

        resource.setId("1233435ff");
        resource.setName("Nombre #1");
        resource.setType("Tipo #1");
        resource.setCategory("Area tematica #1");
        resource.setUnitsAvailable(10);
        resource.setUnitsOwed(5);
        resource.setLastBorrow(LocalDate.parse("2020-01-10"));

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId(resource.getId());
        resourceDTO.setName(resource.getName());
        resourceDTO.setType(resource.getType());
        resourceDTO.setCategory(resource.getCategory());
        resourceDTO.setUnitsAvailable(resource.getUnitsAvailable());
        resourceDTO.setUnitsOwed(resource.getUnitsOwed());
        resourceDTO.setLastBorrow(resource.getLastBorrow());

//        se utiliza para simular el comportamiento del servicio mockeado
//        esto garantiza que cuando se consuma el mockito devuelvan el valor configurado
        // Act
        Mockito.when(resourceRepository.save(any())).thenReturn(Mono.just(resource));

        // Assert
        StepVerifier.create(createResourceUseCase.apply(resourceDTO))
                .expectNextMatches(resourceExpect ->  resourceExpect.getName().equals("Nombre #1"))
                .expectComplete()
                .verify();
    }
}