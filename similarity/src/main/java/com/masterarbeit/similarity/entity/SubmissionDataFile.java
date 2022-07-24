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
        name= "submission_data_file"
)
@Getter
@Setter
public class SubmissionDataFile {
    @Id
    @SequenceGenerator(
            name="submissionDataFile_sequence",
            sequenceName = "submissionDataFile_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "submissionDataFile_sequence"
    )
    @Column(
            name="submissionDataFile_id",
            updatable = false
    )
    private Long submissionDataFileId;
    @Column(
            name ="file_name",
            nullable = false
    )
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "submissionFolderFile_id")
    @JsonBackReference
    private SubmissionFolderFile submissionFolderFile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submissionDataFile", fetch = FetchType.LAZY, orphanRemoval=true)
    @JsonManagedReference
    private List<Fingerprint> fingerprints;

//    @Override
//    public String toString() {
//        return "SubmissionDataFile{" +
//                "id=" + submissionDataFileId  +
//                "folderFileId=" + submissionFolderFile +
//                "fileName=" + fileName  +
//                '}';
//    }

}