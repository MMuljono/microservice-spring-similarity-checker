package com.masterarbeit.extractdocument;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
