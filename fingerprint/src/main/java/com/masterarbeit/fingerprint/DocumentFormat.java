package com.masterarbeit.fingerprint;

import lombok.*;

import java.util.List;

@Data
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

@Data
@NoArgsConstructor
@AllArgsConstructor
class DocumentFileFormat {
    String fileName;
    List<?> fingerprints;
}
