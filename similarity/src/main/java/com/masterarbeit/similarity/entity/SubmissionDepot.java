package com.masterarbeit.similarity.entity;

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
        name= "submission_depot",
        uniqueConstraints={
        @UniqueConstraint(
                name = "depot_key_unique",
                columnNames = {"lecturer_name", "module_name"})
}
)
@Getter
@Setter
public class SubmissionDepot {
    @Id
    @SequenceGenerator(
            name="submission_depot_sequence",
            sequenceName = "submission_depot_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "submission_depot_sequence"
    )
    @Column(
            name="submission_depot_id",
            updatable = false
    )
    private Long SubmissionDepotId;
    @Column(
            name ="lecturer_name"
    )
    private String lecturerName;
    @Column(
            name ="module_name"
    )
    private String moduleName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submissionDepot", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SubmissionSet> submissionSet;

//    @Override
//    public String toString() {
//        return "SubmissionFolderFile{" +
//                "id=" + SubmissionDepot  +
//                "lecturerName=" + lecturerName  +
//                "moduleName=" + moduleName  +
//                '}';
//    }
}
