package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.GenericEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<E extends GenericEntity>
    extends ReactiveMongoRepository<E, String> {}
