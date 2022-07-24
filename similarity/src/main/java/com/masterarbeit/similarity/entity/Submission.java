package com.masterarbeit.similarity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {})
@Table(
        name= "submission",
        uniqueConstraints={
        @UniqueConstraint(
                name="submission_key_unique",
                columnNames = {"submission_name", "submission_set_id"})
        }
)
@Getter
@Setter
public class Submission {
    @Id
    @SequenceGenerator(
            name="submission_sequence",
            sequenceName = "submission_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "submission_sequence"
    )
    @Column(
            name="submission_id"
    )
    private Long submissionId;

    @Column(
            name ="submission_name"
    )
    private String submissionName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "submission_set_id")
    @JsonBackReference
    private SubmissionSet submissionSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submission", fetch = FetchType.LAZY, orphanRemoval=true)
    @JsonManagedReference
    private List<SubmissionFolderFile> submissionFolderFile;

//    @Override
//    public String toString() {
//        return "Submission{" +
//                "id=" + submissionId  +
//                "submissionSet=" + submissionSet +
//                "submissionName=" + submissionName  +
//                '}';
//    }
}
