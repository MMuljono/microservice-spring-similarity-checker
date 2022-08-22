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
@Table(
        name= "submission_folder_file",
        uniqueConstraints={
                @UniqueConstraint(
                    name = "folder_file_key_unique",
                    columnNames = {"folder_name", "submission_id"})
        }
)
@Getter
@Setter
public class SubmissionFolderFile {
    @Id
    @SequenceGenerator(
            name="submission_folder_file_sequence",
            sequenceName = "submission_folder_file_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "submission_folder_file_sequence"
    )
    @Column(
            name="submission_folder_file_id",
            updatable = false
    )
    private Long submissionFolderFileId;
    @Column(
            name ="folder_name",
            nullable = false
    )
    private String folderName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "submission_id")
    @JsonBackReference
    private Submission submission;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submissionFolderFile", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<SubmissionDataFile> submissionDataFiles;

//    @Override
//    public String toString() {
//        return "SubmissionFolderFile{" +
//                "id=" + submissionFolderFileId  +
//                "submissionId=" + submission +
//                "folderName=" + folderName  +
//                '}';
//    }

}