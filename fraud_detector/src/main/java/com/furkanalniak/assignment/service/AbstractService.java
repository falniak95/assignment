package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.GenericEntity;
import com.furkanalniak.assignment.repository.GenericRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Transactional(rollbackFor = Throwable.class)
public abstract class AbstractService<E extends GenericEntity> implements GenericService<E> {

  protected GenericRepository<E> repository;

  protected String generateId() {
    return UUID.randomUUID().toString();
  }

  @Override
  public Mono<E> save(E entity) {
    if (entity.getId() == null) {
      entity.setId(generateId());
      entity.setCreateDate(LocalDateTime.now());
      entity.setUpdateDate(LocalDateTime.now());
    } else {
      entity.setUpdateDate(LocalDateTime.now());
    }
    return repository.save(entity);
  }

  @Override
  public void delete(E entity) {
    repository.delete(entity);
  }

  @Override
  public Mono<E> getById(String id) {
    return repository.findById(id);
  }

  @Override
  public Flux<E> getAll() {
    return repository.findAll();
  }
}
