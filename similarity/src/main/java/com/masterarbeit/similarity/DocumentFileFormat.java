package com.masterarbeit.similarity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFileFormat {
    String fileName;
    List<Integer> fingerprints;
}
