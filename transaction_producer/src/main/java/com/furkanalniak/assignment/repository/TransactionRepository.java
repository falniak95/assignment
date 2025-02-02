package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    // Temel CRUD operasyonları MongoRepository tarafından sağlanacak
    // İhtiyaç duyulan özel sorgular buraya eklenebilir
} 