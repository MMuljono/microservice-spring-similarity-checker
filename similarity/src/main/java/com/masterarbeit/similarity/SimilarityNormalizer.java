package com.masterarbeit.similarity;

import com.masterarbeit.similarity.entity.Submission;
import com.masterarbeit.similarity.entity.SubmissionFolderFile;
import lombok.Data;

import java.util.*;

@Data
public class SimilarityNormalizer {
    private Map<Integer, Integer> mapOfMain;
    private Map<Integer, Integer> mapOfCompare;
    private Map<Integer, Integer> mapOfMainNormalized;
    private Map<Integer, Integer> mapOfCompareNormalized;
    private Set<Integer> allKeysInSet;

    public SimilarityNormalizer(Submission main, Submission compare) {
        mapOfMain = new HashMap<Integer, Integer>();
        main.getSubmissionFolderFile().forEach(f-> {
            f.getSubmissionDataFiles().forEach((e) -> {
                e.getFingerprints().forEach(val -> mapOfMain.put(val.getHashValue(), mapOfMain.getOrDefault(val.getHashValue(), 0) + 1));
            });
        });


        mapOfCompare = new HashMap<Integer, Integer>();
        compare.getSubmissionFolderFile().forEach(f-> {
            f.getSubmissionDataFiles().forEach((e) -> {
                e.getFingerprints().forEach(val -> {
                    mapOfCompare.put(val.getHashValue(), mapOfCompare.getOrDefault(val.getHashValue(), 0) + 1);
                });
            });
        });

        putHasKeyAfterNormalize();
    }

    public SimilarityNormalizer(SubmissionFolderFile main, SubmissionFolderFile compare) {
        mapOfMain = new HashMap<Integer, Integer>();
        main.getSubmissionDataFiles().forEach((e) -> {
            e.getFingerprints().forEach(val -> mapOfMain.put(val.getHashValue(), mapOfMain.getOrDefault(val.getHashValue(), 0) + 1));
        });

        mapOfCompare = new HashMap<Integer, Integer>();
        compare.getSubmissionDataFiles().forEach((e) -> {
            e.getFingerprints().forEach(val -> mapOfCompare.put(val.getHashValue(), mapOfCompare.getOrDefault(val.getHashValue(), 0) + 1));
        });
        putHasKeyAfterNormalize();
    }

    private void putHasKeyAfterNormalize () {
        allKeysInSet = new HashSet<Integer>();
        allKeysInSet.addAll(mapOfMain.keySet());
        allKeysInSet.addAll(mapOfCompare.keySet());
        mapOfMainNormalized = new HashMap<Integer, Integer>();
        mapOfCompareNormalized = new HashMap<Integer, Integer>();
        for(Integer val: allKeysInSet) {
            mapOfMainNormalized.put(val, mapOfMain.getOrDefault(val, 0));
        }
        for(Integer val: allKeysInSet) {
            mapOfCompareNormalized.put(val, mapOfCompare.getOrDefault(val, 0));
        }
    }
}
