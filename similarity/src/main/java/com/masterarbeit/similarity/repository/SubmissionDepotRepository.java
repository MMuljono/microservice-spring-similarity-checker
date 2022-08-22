package com.masterarbeit.similarity.repository;

import com.masterarbeit.similarity.entity.SubmissionDepot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface SubmissionDepotRepository extends JpaRepository<SubmissionDepot,Long> {
   Optional<SubmissionDepot> findFirstByLecturerNameAndAndModuleName(
    String lecturerName, String moduleName);

   @Modifying(flushAutomatically = true)
   @Transactional
   @Query(
           value = "INSERT INTO submission_depot(submission_depot_id, lecturer_name, module_name) VALUES (nextval('submission_depot_sequence'),?1, ?2) ON conflict ON CONSTRAINT depot_key_unique DO NOTHING;" ,
           nativeQuery = true
   )
   void upsertByLecturerNameModuleName(String lecturerName, String moduleName);
}
