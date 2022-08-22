package com.masterarbeit.similarity.repository;

import com.masterarbeit.similarity.entity.SubmissionDepot;
import com.masterarbeit.similarity.entity.SubmissionFolderFile;
import com.masterarbeit.similarity.entity.SubmissionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface SubmissionSetRepository extends JpaRepository<SubmissionSet,Long>{
    Optional<SubmissionSet> findFirstBySubmissionSetNameAndSemester(
            String submissionName, String semester);
    Optional<SubmissionSet> findFirstBySubmissionSetName(String SubmissionSetName);
    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO submission_set(submission_set_id, submission_set_name, semester, assignment_name, submission_depot_id) VALUES (nextval('submission_set_sequence'), ?1, ?2, ?3, ?4) ON conflict ON CONSTRAINT submissionsetid_unique DO NOTHING;" ,
            nativeQuery = true
    )
    void upsertBySubmissionSetNameSemesterDepotId(String submissionSetName, String semester, String assignmentName, Long depotId);
}


