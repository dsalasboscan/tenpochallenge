package com.davidsalas.tenpochallenge.adapter.out.jpa;

import com.davidsalas.tenpochallenge.adapter.out.jpa.model.CalculationJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalculationJpaRepository extends JpaRepository<CalculationJpaModel, UUID> {
    @Query(value = "SELECT * FROM calculations c ORDER BY c.id DESC LIMIT 1", nativeQuery = true)
    Optional<CalculationJpaModel> findLast();
}
