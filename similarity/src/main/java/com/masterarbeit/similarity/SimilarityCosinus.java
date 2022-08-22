package com.masterarbeit.similarity;


import com.masterarbeit.similarity.entity.Submission;
import com.masterarbeit.similarity.entity.SubmissionFolderFile;
import org.springframework.scheduling.annotation.Async;

/**
 * Main Class for calculating cosine similarity
 * */
public class SimilarityCosinus extends SimilarityNormalizer implements Similarity  {
    public SimilarityCosinus(SubmissionFolderFile main, SubmissionFolderFile compare) {
        super(main, compare);
    }

    public SimilarityCosinus(Submission sub1, Submission sub2) { super(sub1, sub2); }

    @Override
    @Async
    public double result() {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for(Integer val : this.getAllKeysInSet()) {
            dotProduct += this.getMapOfMainNormalized().get(val) * this.getMapOfCompareNormalized().get(val);
            normA += Math.pow(this.getMapOfMainNormalized().get(val), 2);
            normB += Math.pow(this.getMapOfCompareNormalized().get(val), 2);
        }
        double result =  dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        return result;
    }
}
