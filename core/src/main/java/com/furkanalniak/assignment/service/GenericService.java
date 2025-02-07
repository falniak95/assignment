package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.GenericEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenericService<E extends GenericEntity> {

  Mono<E> save(E entity);

  void delete(E entity);

  Mono<E> getById(String id);

  Flux<E> getAll();
}
