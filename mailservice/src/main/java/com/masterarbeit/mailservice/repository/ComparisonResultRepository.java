package com.masterarbeit.mailservice.repository;

import com.masterarbeit.mailservice.entity.ComparisonResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComparisonResultRepository extends JpaRepository<ComparisonResult,Long> {
    Optional<ComparisonResult> findAllByUniquePath(String uniquePath);
}
