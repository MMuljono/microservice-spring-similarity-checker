package com.masterarbeit.mailservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Table(
        name= "similarity_summary"
)
@Getter
@Setter
public class SimilaritySummary {
    @Id
    @SequenceGenerator(
            name="SimilaritySummary_sequence",
            sequenceName = "SimilaritySummary_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "SimilaritySummary_sequence"
    )
    @Column(
            name="similarity_summary_id",
            updatable = false
    )
    private Long similaritySummaryId;

    @Column(
            name ="folder_name"
    )
    private String folderName;
    @Column(
            name ="main_submission"
    )
    private String mainSubmission;
    @Column(
            name ="target_submission"
    )
    private String targetSubmission;
    @Column(
            name ="main_submission_set_name"
    )
    private String mainSubmissionSetName;
    @Column(
            name ="target_submission_set_name"
    )
    private String targetSubmissionSetName;
    @Column(
            name ="main_submission_set_semester"
    )
    private String mainSubmissionSetSemester;
    @Column(
            name ="target_submission_set_semester"
    )
    private String targetSubmissionSetSemester;
    @Column(
            name ="similarity_result"
    )
    private String similarityResult;
    @Column(
            name ="distance"
    )
    private String distance;
    @Column(
            name ="unique_hash_in_common"
    )
    private String uniqueHashInCommon;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "comparisonResult_id")
    @JsonBackReference
    private ComparisonResult comparisonResult;
}


