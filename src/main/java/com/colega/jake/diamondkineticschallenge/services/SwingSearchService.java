package com.colega.jake.diamondkineticschallenge.services;

import com.colega.jake.diamondkineticschallenge.exceptions.InvalidInputException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

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

        final RealMatrix dataMatrix = new Array2DRowRealMatrix(new double[][]{data});

        // All problems now captured and thrown
        invalidInputTest(
                dataMatrix,
                indexBegin,
                indexEnd,
                new double[]{threshold},
                new double[]{Double.MAX_VALUE},
                winLength,
                true
        );

        final List<Integer> results = search(
                dataMatrix,
                indexBegin,
                indexEnd,
                winLength,
                (final double[] values) ->
                        values[0] > threshold,
                1,
                true
        );

        if (CollectionUtils.isNotEmpty(results)) {
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

        final RealMatrix dataMatrix = new Array2DRowRealMatrix(new double[][]{data});

        // All problems now captured and thrown
        invalidInputTest(
                dataMatrix,
                indexBegin,
                indexEnd,
                new double[]{thresholdLo},
                new double[]{thresholdHi},
                winLength,
                false
        );

        final List<Integer> results = search(
                dataMatrix,
                indexBegin,
                indexEnd,
                winLength,
                (final double[] values) ->
                        values[0] > thresholdLo && values[0] < thresholdHi,
                1,
                false
        );

        if (CollectionUtils.isNotEmpty(results)) {
            return results.get(0);
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

        final RealMatrix dataMatrix = new Array2DRowRealMatrix(new double[][]{data1, data2});

        // All problems now captured and thrown
        invalidInputTest(
                dataMatrix,
                indexBegin,
                indexEnd,
                new double[]{threshold1, threshold2},
                new double[]{Double.MAX_VALUE, Double.MAX_VALUE},
                winLength,
                true
        );

        final List<Integer> results = search(
                dataMatrix,
                indexBegin,
                indexEnd,
                winLength,
                (final double[] values) ->
                        values[0] > threshold1 && values[1] > threshold2,
                1,
                true
        );

        if (CollectionUtils.isNotEmpty(results)) {
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

        final RealMatrix dataMatrix = new Array2DRowRealMatrix(new double[][]{data});

        // All problems now captured and thrown
        invalidInputTest(
                dataMatrix,
                indexBegin,
                indexEnd,
                new double[]{thresholdLo},
                new double[]{thresholdHi},
                winLength,
                true
        );

        final List<Integer> results = search(
                dataMatrix,
                indexBegin,
                indexEnd,
                winLength,
                (final double[] values) ->
                        values[0] > thresholdLo && values[0] < thresholdHi,
                indexEnd - indexBegin + 1, // impossible number to reach to ensure we get all matches
                true
        );

        int[][] returnValue = new int[results.size()][2];
        for (int i = 0; i < results.size(); i++) {
            returnValue[i] = new int[]{results.get(i), results.get(i) + winLength - 1};
        }

        return returnValue;
    }

    // Common search method implemented
    private List<Integer> search(
            final RealMatrix data,
            final int indexBegin,
            final int indexEnd,
            final int winLength,
            final Predicate<double[]> indexTest,
            final int maxNumMatches,
            final boolean forwardSearch
    ) {

        final List<Integer> potentialIndices = new ArrayList<>();
        final List<Integer> satisfactoryIndices = new ArrayList<>();

        // Moved to exclusive indexEnd
        int i = indexBegin;
        while ((forwardSearch && i < indexEnd) || (!forwardSearch && i > indexEnd)) {

            if (indexTest.evaluate(data.getColumn(i))) {

                potentialIndices.add(i);

                if (CollectionUtils.isNotEmpty(potentialIndices) && i - potentialIndices.get(0) + 1 == winLength) {
                    satisfactoryIndices.add(potentialIndices.remove(0));
                }
            } else {
                potentialIndices.clear();
            }

            if (satisfactoryIndices.size() == maxNumMatches) {
                break;
            }

            i += forwardSearch ? 1 : -1;
        }

        return satisfactoryIndices;
    }

    private void invalidInputTest(
            final RealMatrix data,
            final int indexBegin,
            final int indexEnd,
            final double[] thresholdLos,
            final double[] thresholdHis,
            final int winLength,
            final boolean forwardSearch
    ) {

        final List<String> problems = new ArrayList<>();
        for (int i = 0; i < data.getRowDimension(); i++) {
            problems.addAll(invalidIndicesTest(indexBegin, indexEnd, data.getRow(i).length, forwardSearch));
            problems.addAll(invalidWinLengthTest(winLength, data.getRow(i).length));
        }

        int thresholdLength = Integer.min(thresholdLos.length, thresholdHis.length);
        if (thresholdLos.length != data.getRowDimension()) {
            problems.add("Unequal number of thresholdLo (" + thresholdLos.length + ") and data (" + data.getRowDimension() + ") provided");
        }
        if (thresholdHis.length != data.getRowDimension()) {
            problems.add("Unequal number of thresholdHi (" + thresholdHis.length + ") and data (" + data.getRowDimension() + ") provided");
        }
        for (int i = 0; i < thresholdLength; i++) {
            problems.addAll(invalidThresholdsTest(thresholdLos[i], thresholdHis[i]));
        }


        if (CollectionUtils.isNotEmpty(problems)) {
            throw new InvalidInputException(problems.toString());
        }
    }

    private List<String> invalidIndicesTest(
            final int indexBegin,
            final int indexEnd,
            final int dataLength,
            final boolean forwardSearch
    ) {

        final List<String> problems = new ArrayList<>();
        if (indexBegin < 0) {
            problems.add("indexBegin (" + indexBegin + ") is negative");
        }
        if (indexBegin > dataLength) {
            problems.add("indexBegin (" + indexBegin + ") is too large for number of data provided (" + dataLength + ")");
        }
        if ((forwardSearch && indexEnd < 0) || (!forwardSearch && indexEnd < -1)) {
            problems.add("indexEnd (" + indexEnd + ") is negative");
        }
        if (indexEnd > dataLength) {
            problems.add("indexEnd (" + indexEnd + ") is too large for number of data provided (" + dataLength + ")");
        }
        if ((forwardSearch && indexBegin > indexEnd - 1) || (!forwardSearch && indexBegin < indexEnd + 1)) {
            problems.add("indexBegin (" + indexBegin + ") is greater than or equal to indexEnd (" + indexEnd + ")");
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
