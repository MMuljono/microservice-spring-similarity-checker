package com.masterarbeit.similarity;


import com.masterarbeit.similarity.entity.Submission;
import com.masterarbeit.similarity.entity.SubmissionFolderFile;
import org.springframework.scheduling.annotation.Async;

public class SimilarityEuclianDistance extends SimilarityNormalizer implements Similarity {
    public SimilarityEuclianDistance(SubmissionFolderFile main, SubmissionFolderFile compare) {
        super(main, compare);
    }
    public SimilarityEuclianDistance(Submission sub1, Submission sub2) { super(sub1, sub2); }
    @Override
    @Async
    public double result() {
        double diff_square_sum = 0.0;
        for(Integer val : this.getAllKeysInSet()) {
            diff_square_sum += (this.getMapOfMainNormalized().get(val) - this.getMapOfCompareNormalized().get(val)) * (this.getMapOfMainNormalized().get(val) - this.getMapOfCompareNormalized().get(val));
        }
        double result =  Math.sqrt(diff_square_sum);
        return result;
    }
}
