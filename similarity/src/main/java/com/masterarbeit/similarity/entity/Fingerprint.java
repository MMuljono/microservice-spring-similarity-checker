package com.masterarbeit.similarity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {})
@Table(
        name= "fingerprint"
)
@Getter
@Setter
public class Fingerprint {
    @Id
    @SequenceGenerator(
            name="fingerprint_sequence",
            sequenceName = "fingerprint_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "fingerprint_sequence"
    )
    @Column(
            name="fingerprint_id",
            updatable = false
    )
    private Long fingerprintId;
    @Column(
            name ="hash_value"
    )
    private Integer hashValue;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "submissionDataFile_id")
    @JsonBackReference
    private SubmissionDataFile submissionDataFile;

//    @Override
//    public String toString() {
//        return "Submission{" +
//                "id=" + fingerprintId  +
//                "submissionDataFile=" + submissionDataFile +
//                "hashValue=" + hashValue  +
//                '}';
//    }
}
