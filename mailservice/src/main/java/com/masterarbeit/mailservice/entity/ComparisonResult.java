package com.masterarbeit.mailservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Table(
        name= "comparison_result"
)
@Getter
@Setter
public class ComparisonResult {
    @Id
    @SequenceGenerator(
            name="ComparisonResult_sequence",
            sequenceName = "ComparisonResult_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "ComparisonResult_sequence"
    )
    @Column(
            name="comparisonResult_id",
            updatable = false
    )
    private Long comparisonResultId;
    @Column(
            name ="unique_path"
    )
    private String uniquePath;

    @Column(
            name ="email"
    )
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comparisonResult", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SimilaritySummary> similaritySummaries;
}
