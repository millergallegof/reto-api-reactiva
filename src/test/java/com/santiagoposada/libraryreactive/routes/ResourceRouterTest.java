package com.santiagoposada.libraryreactive.routes;

import com.santiagoposada.libraryreactive.dto.ResourceDTO;
import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import com.santiagoposada.libraryreactive.usecase.CreateResourceUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

//webtestClient se utiliza para hacer pruebas a api rest
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResourceRouterTest {

    @Autowired
    WebTestClient webTestClient;
    @MockBean
    ResourceRepository resourceRepository;
    @SpyBean
    ResourceRouter resourceRouter;
    @SpyBean
    CreateResourceUseCase createResourceUseCase;
    @SpyBean
    ResourceMapper resourceMapper;

    @Test
    void createResourceRoute() {
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

        Flux<Resource> resourceFlux = webTestClient
                .post()
                .uri("/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(resourceTwo))
//                el metodo exchange indica que se tiene que hacer simular una solicitud en el servidor
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Resource.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res.getId(), "987456aa");
                    assertEquals(res.getName(), "Nombre #2");
                })
                .verifyComplete();

    }

    @Test
    void getAllRouter() {
        Resource resourceTwo = new Resource();
        resourceTwo.setId("987456aa");
        resourceTwo.setName("Nombre #2");
        resourceTwo.setType("Tipo #3");
        resourceTwo.setCategory("admon");
        resourceTwo.setUnitsAvailable(8);
        resourceTwo.setUnitsOwed(6);
        resourceTwo.setLastBorrow(LocalDate.parse("2023-10-29"));

        Mockito.when(resourceRepository.findAll()).thenReturn(Flux.just(resourceTwo));

        Flux<Resource> resourceFlux = webTestClient
                .get()
                .uri("/resources")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Resource.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .expectNextMatches(res -> res.getName().equals("Nombre #2"))
                .verifyComplete();

    }

    @Test
    void getResourceById() {
        Resource resourceTwo = new Resource();
        resourceTwo.setId("987456aa");
        resourceTwo.setName("Nombre #2");
        resourceTwo.setType("Tipo #3");
        resourceTwo.setCategory("admon");
        resourceTwo.setUnitsAvailable(8);
        resourceTwo.setUnitsOwed(6);
        resourceTwo.setLastBorrow(LocalDate.parse("2023-10-29"));

        Mockito.when(resourceRepository.findById(Mockito.any(String.class)))
                .thenReturn(Mono.just(resourceTwo));

        Flux<Resource> resourceFlux = webTestClient
                .get()
                .uri("/resource/{id}", "987456aa")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Resource.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .expectNext(resourceTwo)
                .verifyComplete();
    }

    @Test
    void updateResourceRoute() {
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

        Flux<Resource> resourceFlux = webTestClient
                .put()
                .uri("/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(resourceTwo))
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Resource.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .expectNextMatches(res -> res.getCategory().equals("admon"))
                .verifyComplete();
    }

    @Test
    void deleteResourceToute() {
        Mockito.when(resourceRepository.deleteById(any(String.class)))
                .thenReturn(Mono.empty());

        Flux<Resource> resourceFlux = webTestClient
                .delete()
                .uri("/delete/{id}", "987456aa")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isAccepted()
                .returnResult(Resource.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .verifyComplete();

    }

    @Test
    void checkForAvailabilityRoute() {
        Resource resourceTwo = new Resource();
        resourceTwo.setId("987456aa");
        resourceTwo.setName("Nombre #2");
        resourceTwo.setType("Tipo #3");
        resourceTwo.setCategory("admon");
        resourceTwo.setUnitsAvailable(8);
        resourceTwo.setUnitsOwed(6);
        resourceTwo.setLastBorrow(LocalDate.parse("2023-10-29"));

        Mockito.when(resourceRepository.findById(any(String.class)))
                .thenReturn(Mono.just(resourceTwo));

        Flux<String> resourceFlux = webTestClient
                .get()
                .uri("/availability/{id}", "987456aa")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .expectNextMatches(res -> res.equals("Nombre #2" + "is available"))
                .verifyComplete();
    }

    @Test
    void getByTypeRoute() {
        Resource resourceTwo = new Resource();
        resourceTwo.setId("987456aa");
        resourceTwo.setName("Nombre #2");
        resourceTwo.setType("Tipo #3");
        resourceTwo.setCategory("admon");
        resourceTwo.setUnitsAvailable(8);
        resourceTwo.setUnitsOwed(6);
        resourceTwo.setLastBorrow(LocalDate.parse("2023-10-29"));

        Mockito.when(resourceRepository.findAllByType(any(String.class)))
                .thenReturn(Flux.just(resourceTwo));

        Flux<Resource> resourceFlux = webTestClient
                .get()
                .uri("/getByType/{type}", "Tipo #3")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Resource.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res.getId(), "987456aa");
                })
                .verifyComplete();
    }

    @Test
    void getByCategory() {
        Resource resourceTwo = new Resource();
        resourceTwo.setId("987456aa");
        resourceTwo.setName("Nombre #2");
        resourceTwo.setType("Tipo #3");
        resourceTwo.setCategory("admon");
        resourceTwo.setUnitsAvailable(8);
        resourceTwo.setUnitsOwed(6);
        resourceTwo.setLastBorrow(LocalDate.parse("2023-10-29"));

        Mockito.when(resourceRepository.findAllByCategory(any(String.class)))
                .thenReturn(Flux.just(resourceTwo));

        Flux<Resource> resourceFlux = webTestClient
                .get()
                .uri("/getByCategory/{category}", "admon")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Resource.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res.getId(), "987456aa");
                })
                .verifyComplete();
    }

    @Test
    void borrowResourceRoute() {
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

        Flux<String> resourceFlux = webTestClient
                .put()
                .uri("/borrow/{id}", "987456aa")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .expectNextMatches(res -> res.equals("The resource "
                        + "Nombre #2" + " has been borrowed, there are "
                        + "7" + " units available"))
                .verifyComplete();
    }

    @Test
    void addUnitsAvailable() {
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
        resourceUpdate.setUnitsAvailable(13);
        resourceUpdate.setUnitsOwed(6);
        resourceUpdate.setLastBorrow(LocalDate.parse("2023-10-29"));

        Mockito.when(resourceRepository.findById(any(String.class)))
                .thenReturn(Mono.just(resourceTwo));
        Mockito.when(resourceRepository.save(any(Resource.class)))
                .thenReturn(Mono.just(resourceUpdate));

        Flux<Resource> resourceFlux = webTestClient
                .put()
                .uri("/add/{id}/{units}", "987456aa", 5)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Resource.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res.getId(),"987456aa" );
                    assertEquals(res.getUnitsAvailable(), 13);
                })
                .verifyComplete();
    }

    @Test
    void returnRoute() {
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
        resourceUpdate.setUnitsAvailable(13);
        resourceUpdate.setUnitsOwed(6);
        resourceUpdate.setLastBorrow(LocalDate.parse("2023-10-29"));

        Mockito.when(resourceRepository.findById(any(String.class)))
                .thenReturn(Mono.just(resourceTwo));
        Mockito.when(resourceRepository.save(any(Resource.class)))
                .thenReturn(Mono.just(resourceUpdate));

        Flux<String> resourceFlux = webTestClient
                .put()
                .uri("/return/{id}", "987456aa")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier
                .create(resourceFlux)
                .expectSubscription()
                .consumeNextWith(res -> {
                    assertNotNull(res);
                    assertEquals(res, "The resource with id: "
                            + "987456aa" + "was returned successfully");
                })
                .verifyComplete();
    }
}