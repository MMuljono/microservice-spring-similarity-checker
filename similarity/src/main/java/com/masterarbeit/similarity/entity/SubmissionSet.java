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
        name= "submission_set",
        uniqueConstraints = @UniqueConstraint(
        name="submissionSetId_unique",
        columnNames="submissionSet_name"
        )
)
@Getter
@Setter
public class SubmissionSet {
    @Id
    @SequenceGenerator(
            name="submission_set_sequence",
            sequenceName = "submission_set_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "submission_set_sequence"
    )
    @Column(
            name="submission_set_id"
    )
    private Long submissionSetId;

    @Column(
            name ="submissionSet_name"
    )
    private String submissionSetName;
    @Column(
            name ="semester"
    )
    private String semester;

    @Column(
            name ="assignment_name"
    )
    private String assignment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SubmissionDepot_id")
    @JsonBackReference
    private SubmissionDepot submissionDepot;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submissionSet", fetch = FetchType.LAZY, orphanRemoval= true)
    @JsonManagedReference
    private List<Submission> submissions;

//    @Override
//    public String toString() {
//        return "SubmissionSet{" +
//                "id=" + submissionSetId  +
//                "submissionSetName=" + submissionSetName  +
//                "semester=" + semester +
//                "submissionDepotId=" + submissionDepot +
//                '}';
//    }

}
