package com.masterarbeit.similarity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFormat {
    String lecturer;
    String module;
    String assignment;
    String semester;
    String submissionSetName;
    String submissionName;
    String folderName;
    List<DocumentFileFormat> content;
}

