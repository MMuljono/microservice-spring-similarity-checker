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
public interface SubmissionFolderFileRepository extends JpaRepository<SubmissionFolderFile, Long>{
    Optional<List<SubmissionFolderFile>> findAllByFolderName(String folderName);
    Optional<SubmissionFolderFile> findFirstByFolderNameAndSubmission(String SubmissionSetName, Submission Submission);
    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO submission_folder_file(submission_folder_file_id, folder_name, submission_id) VALUES (nextval('submission_folder_file_sequence'), ?1, ?2) ON conflict ON CONSTRAINT folder_file_key_unique DO NOTHING;" ,
            nativeQuery = true
    )
    void upsertByFolderName(String folderName, Long submissionId);
}

