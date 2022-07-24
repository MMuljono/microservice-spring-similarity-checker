package com.masterarbeit.similarity;


import com.masterarbeit.similarity.entity.Submission;
import com.masterarbeit.similarity.entity.SubmissionFolderFile;
import org.springframework.scheduling.annotation.Async;

import java.util.HashSet;
import java.util.Set;

public class SimilarityJaccard extends SimilarityNormalizer implements Similarity {
    public SimilarityJaccard(SubmissionFolderFile main, SubmissionFolderFile compare) {
        super(main, compare);
    }
    public SimilarityJaccard(Submission sub1, Submission sub2) { super(sub1, sub2); }
    @Override
    @Async
    public double result() {
        Set<Integer> newset = new HashSet<>(this.getMapOfMain().keySet());
        newset.retainAll(this.getMapOfCompare().keySet());
        float result = (float)newset.size() / this.getAllKeysInSet().size();
        return result;
    }
}
