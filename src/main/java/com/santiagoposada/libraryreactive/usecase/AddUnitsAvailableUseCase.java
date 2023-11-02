package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.dto.ResourceDTO;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@Validated
//bifunction recibe dos parametros
//function y crear un dto con los 2 atributos
//crear una interfaz funcional con un solo metodo
public class AddUnitsAvailableUseCase implements BiFunction<String, Integer, Mono<ResourceDTO>> {

    //    @Autowired
    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final UpdateUseCase updateUseCase;

    public AddUnitsAvailableUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper, UpdateUseCase updateUseCase) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.updateUseCase = updateUseCase;
    }

    @Override
    public Mono<ResourceDTO> apply(String id, Integer units) {
        Objects.requireNonNull(id, "Id required to add resource");
        return resourceRepository.findById(id)
                .flatMap(resource -> {
                    resource.setUnitsAvailable(resource.getUnitsAvailable() + units);
                    return updateUseCase.apply(resourceMapper.fromResourceEntityToDTO().apply(resource));
                });
    }

}
