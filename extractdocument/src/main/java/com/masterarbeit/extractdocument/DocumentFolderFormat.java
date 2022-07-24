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
public class DocumentFolderFormat {
    String folderName;
    List<DocumentFileFormat> files;
}
