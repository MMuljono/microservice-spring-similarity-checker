package com.masterarbeit.similarity.repository;

import com.masterarbeit.similarity.entity.Fingerprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint,Long> {
}
