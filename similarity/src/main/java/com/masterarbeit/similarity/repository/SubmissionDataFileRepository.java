package com.masterarbeit.similarity.repository;

import com.masterarbeit.similarity.entity.SubmissionDataFile;
import com.masterarbeit.similarity.entity.SubmissionFolderFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionDataFileRepository extends JpaRepository<SubmissionDataFile,Long> {
    Optional<SubmissionDataFile> findFirstByFileNameAndSubmissionFolderFile(String fileName, SubmissionFolderFile folder);
}
