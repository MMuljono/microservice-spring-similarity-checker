package com.masterarbeit.similarity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class SimilarityMailFormat {
    String email;
    List<SimilaritySummary> summary;
}
