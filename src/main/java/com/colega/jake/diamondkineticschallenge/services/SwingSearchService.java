package com.colega.jake.diamondkineticschallenge.services;

import com.colega.jake.diamondkineticschallenge.exceptions.InvalidIndicesException;
import com.colega.jake.diamondkineticschallenge.exceptions.InvalidThresholdsException;
import com.colega.jake.diamondkineticschallenge.exceptions.InvalidWinLengthException;
import org.apache.commons.collections4.CollectionUtils;

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

        invalidIndicesTest(indexBegin, indexEnd, data.length);
        invalidWinLengthTest(winLength, data.length);

        int currentIndex = -1;
        int currentLength = 0;

        int i = indexBegin;
        while (i <= indexEnd) {

            if (data[i] > threshold) {
                if (currentLength == 0) {
                    currentIndex = i;
                }

                currentLength++;

                if (currentLength == winLength) {
                    return currentIndex;
                }
            } else if (currentLength != 0) {
                currentLength = 0;
            }

            i++;
        }

        return -1;
    }

    public int backSearchContinuityWithinRange(
            final double[] data,
            final int indexBegin,
            final int indexEnd,
            final double thresholdLo,
            final double thresholdHi,
            final int winLength
    ) {

        invalidIndicesTest(indexEnd, indexBegin, data.length);
        invalidWinLengthTest(winLength, data.length);
        invalidThresholdsTest(thresholdLo, thresholdHi);

        int currentIndex = -1;
        int currentLength = 0;

        int i = indexBegin;
        while (i >= indexEnd) {

            if (data[i] > thresholdLo && data[i] < thresholdHi) {
                if (currentLength == 0) {
                    currentIndex = i;
                }

                currentLength++;

                if (currentLength == winLength) {
                    return currentIndex;
                }
            } else if (currentLength != 0) {
                currentLength = 0;
            }

            i--;
        }

        return -1;
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

        invalidIndicesTest(indexBegin, indexEnd, data1.length);
        invalidIndicesTest(indexBegin, indexEnd, data2.length);
        invalidWinLengthTest(winLength, data1.length);
        invalidWinLengthTest(winLength, data2.length);

        int currentIndex = -1;
        int currentLength = 0;

        int i = indexBegin;
        while (i <= indexEnd) {

            if (data1[i] > threshold1 && data2[i] > threshold2) {
                if (currentLength == 0) {
                    currentIndex = i;
                }

                currentLength++;

                if (currentLength == winLength) {
                    return currentIndex;
                }
            } else if (currentLength != 0) {
                currentLength = 0;
            }

            i++;
        }

        return -1;
    }

    public int[][] searchMultiContinuityWithinRange(
            final double[] data,
            final int indexBegin,
            final int indexEnd,
            final double thresholdLo,
            final double thresholdHi,
            final int winLength
    ) {

        invalidIndicesTest(indexBegin, indexEnd, data.length);
        invalidWinLengthTest(winLength, data.length);
        invalidThresholdsTest(thresholdLo, thresholdHi);

        List<Integer> potentialIndices = new ArrayList<>();
        List<Integer> satisfactoryIndices = new ArrayList<>();

        int i = indexBegin;
        while (i <= indexEnd) {

            if (data[i] > thresholdLo && data[i] < thresholdHi) {

                potentialIndices.add(i);

                if (CollectionUtils.isNotEmpty(potentialIndices) && i - potentialIndices.get(0) + 1 == winLength) {
                    satisfactoryIndices.add(potentialIndices.remove(0));
                }
            } else {
                potentialIndices.clear();
            }

            i++;
        }

        int[][] results = new int[satisfactoryIndices.size()][2];
        final int numSatisfactoryIndices = satisfactoryIndices.size();

        i = 0;
        while (i < numSatisfactoryIndices) {

            final int beginIndex = satisfactoryIndices.remove(0);
            results[i] = new int[]{beginIndex, beginIndex + winLength - 1};

            i++;
        }

        return results;
    }

    private void invalidIndicesTest(final int indexBegin, final int indexEnd, final int dataLength) {

        final List<String> problems = new ArrayList<>();
        if (indexBegin < 0) {
            problems.add("indexBegin (" + indexBegin + ") is negative");
        }
        if (indexBegin >= dataLength) {
            problems.add("indexBegin (" + indexBegin + ") is too large for number of data provided (" + dataLength + ")");
        }
        if (indexEnd < 0) {
            problems.add("indexEnd (" + indexEnd + ") is negative");
        }
        if (indexEnd >= dataLength) {
            problems.add("indexEnd (" + indexEnd + ") is too large for number of data provided (" + dataLength + ")");
        }
        if (indexBegin > indexEnd) {
            problems.add("indexBegin (" + indexBegin + ") is greater than indexEnd (" + indexEnd + ")");
        }

        if (CollectionUtils.isNotEmpty(problems)) {
            throw new InvalidIndicesException(problems.toString());
        }
    }

    private void invalidWinLengthTest(final int winLength, final int dataLength) {

        final List<String> problems = new ArrayList<>();
        if (winLength <= 0) {
            problems.add("winLength (" + winLength + ") is not positive");
        }
        if (winLength >= dataLength) {
            problems.add("winLength (" + winLength + ") is too large for number of data provided (" + dataLength + ")");
        }

        if (CollectionUtils.isNotEmpty(problems)) {
            throw new InvalidWinLengthException(problems.toString());
        }
    }

    private void invalidThresholdsTest(final double thresholdLo, final double thresholdHi) {

        final List<String> problems = new ArrayList<>();
        if (thresholdLo > thresholdHi) {
            problems.add("thresholdLo (" + thresholdLo + ") is greater than thresholdHi (" + thresholdHi + ")");
        }

        if (CollectionUtils.isNotEmpty(problems)) {
            throw new InvalidThresholdsException(problems.toString());
        }
    }
}
