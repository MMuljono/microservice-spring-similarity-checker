package com.masterarbeit.similarity.repository;

import com.masterarbeit.similarity.entity.Submission;
import com.masterarbeit.similarity.entity.SubmissionFolderFile;
import com.masterarbeit.similarity.entity.SubmissionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission,Long> {
    Optional<Submission> findFirstBySubmissionName(String submissionName);

    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO submission(submission_id, submission_name, submission_set_id) VALUES (nextval('submission_id_seq'), ?1, ?2) ON conflict ON CONSTRAINT submission_key_unique DO NOTHING;" ,
            nativeQuery = true
    )
    void upsertBySubmissionName(String submissionName, Long submissionSetId);
}
