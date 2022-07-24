package com.masterarbeit.similarity;


import lombok.*;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class SimilaritySummary {
    String folderName;
    String mainSubmission;
    String targetSubmission;
    String mainSubmissionSetName;
    String targetSubmissionSetName;
    String mainSubmissionSetSemester;
    String targetSubmissionSetSemester;
    String similarityResult;
    String distance;
    String uniqueHashInCommon;
}

