package com.colega.jake.diamondkineticschallenge.services;

import com.colega.jake.diamondkineticschallenge.exceptions.InvalidInputException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.List;

public class SwingSearchService {

    public int searchContinuityAboveValue(
            final double[] data,
            final int indexBegin,
            final int indexEnd,
            final double threshold,
            final int winLength
    ) {

        final double[][] standardizedData = new double[][]{data};
        final double[] standardizedThresholdLo = new double[]{threshold};
        final double[] standardizedThresholdHi = new double[]{Double.MAX_VALUE};

        // All problems now captured and thrown
        invalidInputTest(
                standardizedData,
                indexBegin,
                indexEnd,
                standardizedThresholdLo,
                standardizedThresholdHi,
                winLength
        );

        final List<Integer> results = search(
                standardizedData,
                indexBegin,
                indexEnd,
                standardizedThresholdLo,
                standardizedThresholdHi,
                winLength
        );

        if (results.size() > 0) {
            return results.get(0);
        } else {
            return -1;
        }
    }

    public int backSearchContinuityWithinRange(
            final double[] data,
            final int indexBegin,
            final int indexEnd,
            final double thresholdLo,
            final double thresholdHi,
            final int winLength
    ) {

        final double[][] standardizedData = new double[][]{data};
        final double[] standardizedThresholdLo = new double[]{thresholdLo};
        final double[] standardizedThresholdHi = new double[]{thresholdHi};

        // All problems now captured and thrown
        invalidInputTest(
                standardizedData,
                indexEnd,
                indexBegin,
                standardizedThresholdLo,
                standardizedThresholdHi,
                winLength
        );

        final List<Integer> results = search(
                standardizedData,
                indexEnd,
                indexBegin,
                standardizedThresholdLo,
                standardizedThresholdHi,
                winLength
        );

        if (results.size() > 0) {
            return results.get(results.size() - 1);
        } else {
            return -1;
        }
    }

    public int searchContinuityAboveValueTwoSignals(
            final double[] data1,
            final double[] data2,
            final int indexBegin,
            final int indexEnd,
            final double threshold1,
            final double threshold2,
            final int winLength
    ) {

        final double[][] standardizedData = new double[][]{data1, data2};
        final double[] standardizedThresholdLo = new double[]{threshold1, threshold2};
        final double[] standardizedThresholdHi = new double[]{Double.MAX_VALUE, Double.MAX_VALUE};

        // All problems now captured and thrown
        invalidInputTest(
                standardizedData,
                indexBegin,
                indexEnd,
                standardizedThresholdLo,
                standardizedThresholdHi,
                winLength
        );

        final List<Integer> results = search(
                standardizedData,
                indexBegin,
                indexEnd,
                standardizedThresholdLo,
                standardizedThresholdHi,
                winLength
        );

        if (results.size() > 0) {
            return results.get(0);
        } else {
            return -1;
        }
    }

    public int[][] searchMultiContinuityWithinRange(
            final double[] data,
            final int indexBegin,
            final int indexEnd,
            final double thresholdLo,
            final double thresholdHi,
            final int winLength
    ) {

        final double[][] standardizedData = new double[][]{data};
        final double[] standardizedThresholdLo = new double[]{thresholdLo};
        final double[] standardizedThresholdHi = new double[]{thresholdHi};

        // All problems now captured and thrown
        invalidInputTest(
                standardizedData,
                indexBegin,
                indexEnd,
                standardizedThresholdLo,
                standardizedThresholdHi,
                winLength
        );

        final List<Integer> results = search(
                standardizedData,
                indexBegin,
                indexEnd,
                standardizedThresholdLo,
                standardizedThresholdHi,
                winLength
        );

        int[][] returnValue = new int[results.size()][2];
        for (int i = 0; i < results.size(); i++) {
            returnValue[i] = new int[]{results.get(i), results.get(i) + winLength - 1};
        }

        return returnValue;
    }

    // Common search method implemented
    private List<Integer> search(
            final double[][] data,
            final int indexBegin,
            final int indexEnd,
            final double[] thresholdLo,
            final double[] thresholdHi,
            final int winLength
    ) {

        final List<Integer> potentialIndices = new ArrayList<>();
        final List<Integer> satisfactoryIndices = new ArrayList<>();

        // Moved to exclusive indexEnd
        for (int i = indexBegin; i < indexEnd; i++) {

            final MutableBoolean allSuccessful = new MutableBoolean(true);

            for (int j = 0; j < data.length; j++) {

                if (data[j][i] <= thresholdLo[j] || data[j][i] >= thresholdHi[j]) {
                    allSuccessful.setFalse();
                    break;
                }
            }

            if (allSuccessful.isTrue()) {

                potentialIndices.add(i);

                if (CollectionUtils.isNotEmpty(potentialIndices) && i - potentialIndices.get(0) + 1 == winLength) {
                    satisfactoryIndices.add(potentialIndices.remove(0));
                }
            } else {
                potentialIndices.clear();
            }
        }

        return satisfactoryIndices;
    }

    private void invalidInputTest(
            final double[][] data,
            final int indexBegin,
            final int indexEnd,
            final double[] thresholdLo,
            final double[] thresholdHi,
            final int winLength
    ) {

        final List<String> problems = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            problems.addAll(invalidIndicesTest(indexBegin, indexEnd, data[i].length));
            problems.addAll(invalidWinLengthTest(winLength, data[i].length));
        }
        int thresholdLength = thresholdLo.length;
        if (thresholdLo.length != thresholdHi.length) {
            problems.add("Unequal number of thresholdLo (" + thresholdLo.length + ") and thresholdHi (" + thresholdHi.length + ") provided");
            thresholdLength = Integer.min(thresholdLo.length, thresholdHi.length);
        }
        if (thresholdLo.length != data.length) {
            problems.add("Unequal number of thresholdLo (" + thresholdLo.length + ") and data (" + data.length + ") provided");
        }
        if (thresholdHi.length != data.length) {
            problems.add("Unequal number of thresholdHi (" + thresholdHi.length + ") and data (" + data.length + ") provided");
        }
        for (int i = 0; i < thresholdLength; i++) {
            problems.addAll(invalidThresholdsTest(thresholdLo[i], thresholdHi[i]));
        }

        if (CollectionUtils.isNotEmpty(problems)) {
            throw new InvalidInputException(problems.toString());
        }
    }

    private List<String> invalidIndicesTest(final int indexBegin, final int indexEnd, final int dataLength) {

        final List<String> problems = new ArrayList<>();
        if (indexBegin < 0) {
            problems.add("indexBegin (" + indexBegin + ") is negative");
        }
        if (indexBegin > dataLength) {
            problems.add("indexBegin (" + indexBegin + ") is too large for number of data provided (" + dataLength + ")");
        }
        if (indexEnd < 0) {
            problems.add("indexEnd (" + indexEnd + ") is negative");
        }
        if (indexEnd > dataLength) {
            problems.add("indexEnd (" + indexEnd + ") is too large for number of data provided (" + dataLength + ")");
        }
        if (indexBegin > indexEnd) {
            problems.add("indexBegin (" + indexBegin + ") is greater than indexEnd (" + indexEnd + ")");
        }

        return problems;
    }

    private List<String> invalidWinLengthTest(final int winLength, final int dataLength) {

        final List<String> problems = new ArrayList<>();
        if (winLength <= 0) {
            problems.add("winLength (" + winLength + ") is not positive");
        }
        if (winLength >= dataLength) {
            problems.add("winLength (" + winLength + ") is too large for number of data provided (" + dataLength + ")");
        }

        return problems;
    }

    private List<String> invalidThresholdsTest(final double thresholdLo, final double thresholdHi) {

        final List<String> problems = new ArrayList<>();
        if (thresholdLo > thresholdHi) {
            problems.add("thresholdLo (" + thresholdLo + ") is greater than thresholdHi (" + thresholdHi + ")");
        }

        return problems;
    }
}
