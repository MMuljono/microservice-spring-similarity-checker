package com.masterarbeit.fingerprint;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class FingerprintWinnowingService {
    private static Integer kgrams = 33;
    private static Integer winsize = 23;

    private static List hashingToken(List<String> tokens) {
        ArrayList<Integer> hashInArray =  new ArrayList<Integer>();
        for (String token : tokens) {
            String hashedToken = Hashing.sha256()
            .hashString(token, StandardCharsets.UTF_8)
            .toString();
            String subset = hashedToken;
            if (hashedToken.length() > 4) {
                subset = hashedToken.substring(hashedToken.length()-4);
            }
            hashInArray.add(parseInt(subset,16));
        }
        return winnowing(hashInArray);
    }

    public static List groupingToken(List<String> listOfText) {
        String text = String.join("", listOfText);
        int length = text.length();
        List<String> pieces = new ArrayList<>();
        for(int i = 0; i < length - kgrams + 1 ; i++) {
            pieces.add(text.substring(i, i+ kgrams));
        }
        return hashingToken(pieces);
    }

    private static int minimumIndex(List<Integer> win) {
        int minValue = win.get(0);
        int minIndex = 0;
        int length = win.size();
        for (int i = 0; i < length; i++) {
            if (win.get(i) < minValue) {
                minValue = win.get(i);
                minIndex = i;
            }
        }
        return minIndex;
    }

    private static List winnowing(ArrayList<Integer> tokens) {
        int minCur = 0;
        int minPrev = 0;
        int length = tokens.size();
        List<List<Integer>> windows =  new ArrayList<>();
        List<Integer> fingerprint =  new ArrayList<Integer>();
        for(int i = 0; i < length - winsize + 1 ; i++) {
            List<Integer> window = tokens.subList(i, i+winsize);
            windows.add(window);
            minCur = i + minimumIndex(window);
            if (minCur != minPrev) {
                fingerprint.add(tokens.get(minCur));
                minPrev = minCur;
            }
        }
        return fingerprint;
    }
}
