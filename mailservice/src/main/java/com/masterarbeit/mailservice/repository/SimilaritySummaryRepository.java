package com.masterarbeit.mailservice.repository;

import com.masterarbeit.mailservice.entity.SimilaritySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimilaritySummaryRepository extends JpaRepository<SimilaritySummary,Long> {
}
