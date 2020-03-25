package com.colega.jake.diamondkineticschallenge.services;

import com.colega.jake.diamondkineticschallenge.exceptions.InvalidInputException;
import com.colega.jake.diamondkineticschallenge.models.Swing;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class SwingSearchServiceTest {

    private static final String TEST_FILEPATH = "src/test/resources/test_swing.csv";

    private static Swing testSwing;
    private static SwingSearchService swingSearchService;

    @BeforeAll
    static void setup() {

        testSwing = new Swing(new File(TEST_FILEPATH));

        swingSearchService = new SwingSearchService();
    }

    @Test
    void searchContinuityAboveValue_thresholdZero_20Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.searchContinuityAboveValue(
                testData,
                0,
                testData.length,
                0,
                1
        );

        assertEquals(20, result);
    }

    @Test
    void searchContinuityAboveValue_thresholdNegative_4Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.searchContinuityAboveValue(
                testData,
                0,
                testData.length,
                -1,
                1
        );

        assertEquals(4, result);
    }

    @Test
    void searchContinuityAboveValue_thresholdPositive_37Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.searchContinuityAboveValue(
                testData,
                0,
                testData.length,
                1,
                1
        );

        assertEquals(37, result);
    }

    @Test
    void searchContinuityAboveValue_thresholdDouble_39Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.searchContinuityAboveValue(
                testData,
                0,
                testData.length,
                1.1,
                1
        );

        assertEquals(39, result);
    }

    @Test
    void searchContinuityAboveValue_noMatches_negativeOneReturned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.searchContinuityAboveValue(
                testData,
                0,
                testData.length,
                999,
                1
        );

        assertEquals(-1, result);
    }

    @Test
    void searchContinuityAboveValue_winLengthTen_20Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.searchContinuityAboveValue(
                testData,
                0,
                testData.length,
                0,
                1
        );

        assertEquals(20, result);
    }

    @Test
    void searchContinuityAboveValue_winLengthIsNotMet_negativeOneReturned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.searchContinuityAboveValue(
                testData,
                0,
                100,
                1.4,
                10
        );

        assertEquals(-1, result);
    }

    @Test
    void searchContinuityAboveValue_winLengthZero_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.searchContinuityAboveValue(
                                testData,
                                0,
                                testData.length,
                                0,
                                0
                        )
        );
    }

    @Test
    void searchContinuityAboveValue_winLengthIsNegative_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.searchContinuityAboveValue(
                                testData,
                                0,
                                testData.length,
                                0,
                                -1
                        )
        );
    }

    @Test
    void searchContinuityAboveValue_winLengthIsLongerThanData_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.searchContinuityAboveValue(
                                testData,
                                0,
                                testData.length,
                                0,
                                testData.length + 1
                        )
        );
    }

    @Test
    void searchContinuityAboveValue_beginIndexIsNegative_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.searchContinuityAboveValue(
                                testData,
                                -1,
                                testData.length,
                                0,
                                1
                        )
        );
    }

    @Test
    void searchContinuityAboveValue_beginIndexIsLargerThanData_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.searchContinuityAboveValue(
                                testData,
                                testData.length + 1,
                                testData.length,
                                0,
                                1
                        )
        );
    }

    @Test
    void searchContinuityAboveValue_endIndexIsNegative_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.searchContinuityAboveValue(
                                testData,
                                0,
                                -1,
                                0,
                                1
                        )
        );
    }

    @Test
    void searchContinuityAboveValue_endIndexIsLargerThanData_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.searchContinuityAboveValue(
                                testData,
                                0,
                                testData.length + 1,
                                0,
                                1
                        )
        );
    }

    @Test
    void searchContinuityAboveValue_endIndexLessThanBeginIndex_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.searchContinuityAboveValue(
                                testData,
                                1,
                                0,
                                0,
                                1
                        )
        );
    }

    @Test
    void backSearchContinuityWithinRange_thresholdLoZero_1264Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                0,
                999,
                1
        );

        assertEquals(1264, result);
    }

    @Test
    void backSearchContinuityWithinRange_thresholdHiZero_1275Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                -999,
                0,
                1
        );

        assertEquals(1275, result);
    }

    @Test
    void backSearchContinuityWithinRange_thresholdLoNegative_1275Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                -1,
                999,
                1
        );

        assertEquals(1275, result);
    }

    @Test
    void backSearchContinuityWithinRange_thresholdHiNegative_955Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                -999,
                -1,
                1
        );

        assertEquals(955, result);
    }

    @Test
    void backSearchContinuityWithinRange_thresholdLoPositive_924Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                1,
                999,
                1
        );

        assertEquals(924, result);
    }

    @Test
    void backSearchContinuityWithinRange_thresholdHiPositive_1275Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                -999,
                1,
                1
        );

        assertEquals(1275, result);
    }

    @Test
    void backSearchContinuityWithinRange_thresholdLoDouble_924Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                1.1,
                999,
                1
        );

        assertEquals(924, result);
    }

    @Test
    void backSearchContinuityWithinRange_thresholdHiDouble_1275Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                -999,
                1.1,
                1
        );

        assertEquals(1275, result);
    }

    @Test
    void backSearchContinuityWithinRange_noMatches_negativeOneReturned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                999,
                1000,
                1
        );

        assertEquals(-1, result);
    }

    @Test
    void backSearchContinuityWithinRange_winLengthTen_1264Returned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                testData.length - 1,
                -1,
                0,
                999,
                1
        );

        assertEquals(1264, result);
    }

    @Test
    void backSearchContinuityWithinRange_winLengthIsNotMet_negativeOneReturned() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int result = swingSearchService.backSearchContinuityWithinRange(
                testData,
                100,
                0,
                1.4,
                999,
                10
        );

        assertEquals(-1, result);
    }

    @Test
    void backSearchContinuityWithinRange_thresholdLoGreaterThanThresholdHi_exceptionThrown() {

        final double[] testData = testSwing.getAcceleratorXData();

        assertThrows(
                InvalidInputException.class,
                () ->
                        swingSearchService.backSearchContinuityWithinRange(
                                testData,
                                100,
                                0,
                                1,
                                0,
                                10
                        )
        );
    }

    @Test
    void searchContinuityAboveValueTwoSignals_thresholdZero_20Returned() {

        final double[] testData1 = testSwing.getAcceleratorXData();
        final double[] testData2 = testSwing.getAcceleratorYData();

        final int result = swingSearchService.searchContinuityAboveValueTwoSignals(
                testData1,
                testData2,
                0,
                testData1.length - 1,
                0,
                0,
                1
        );

        assertEquals(20, result);
    }

    @Test
    void searchContinuityAboveValueTwoSignals_thresholdNegative_4Returned() {

        final double[] testData1 = testSwing.getAcceleratorXData();
        final double[] testData2 = testSwing.getAcceleratorYData();

        final int result = swingSearchService.searchContinuityAboveValueTwoSignals(
                testData1,
                testData2,
                0,
                testData1.length - 1,
                -1,
                -1,
                1
        );

        assertEquals(4, result);
    }

    @Test
    void searchContinuityAboveValueTwoSignals_thresholdPositive_37Returned() {

        final double[] testData1 = testSwing.getAcceleratorXData();
        final double[] testData2 = testSwing.getAcceleratorYData();

        final int result = swingSearchService.searchContinuityAboveValueTwoSignals(
                testData1,
                testData2,
                0,
                testData1.length - 1,
                1,
                1,
                1
        );

        assertEquals(37, result);
    }

    @Test
    void searchContinuityAboveValueTwoSignals_thresholdDouble_42Returned() {

        final double[] testData1 = testSwing.getAcceleratorXData();
        final double[] testData2 = testSwing.getAcceleratorYData();

        final int result = swingSearchService.searchContinuityAboveValueTwoSignals(
                testData1,
                testData2,
                0,
                testData1.length - 1,
                1.1,
                1.1,
                1
        );

        assertEquals(42, result);
    }

    @Test
    void searchContinuityAboveValueTwoSignals_noMatches_negativeOneReturned() {

        final double[] testData1 = testSwing.getAcceleratorXData();
        final double[] testData2 = testSwing.getAcceleratorYData();

        final int result = swingSearchService.searchContinuityAboveValueTwoSignals(
                testData1,
                testData2,
                0,
                testData1.length - 1,
                999,
                999,
                1
        );

        assertEquals(-1, result);
    }

    @Test
    void searchContinuityAboveValueTwoSignals_winLengthTen_20Returned() {

        final double[] testData1 = testSwing.getAcceleratorXData();
        final double[] testData2 = testSwing.getAcceleratorYData();

        final int result = swingSearchService.searchContinuityAboveValueTwoSignals(
                testData1,
                testData2,
                0,
                testData1.length - 1,
                0,
                0,
                10
        );

        assertEquals(20, result);
    }

    @Test
    void searchContinuityAboveValueTwoSignals_winLengthIsNotMetData1_negativeOneReturned() {

        final double[] testData1 = testSwing.getAcceleratorXData();
        final double[] testData2 = testSwing.getAcceleratorYData();

        final int result = swingSearchService.searchContinuityAboveValueTwoSignals(
                testData1,
                testData2,
                0,
                100,
                1.4,
                0,
                10
        );

        assertEquals(-1, result);
    }

    @Test
    void searchContinuityAboveValueTwoSignals_winLengthIsNotMetData2_negativeOneReturned() {

        final double[] testData1 = testSwing.getAcceleratorXData();
        final double[] testData2 = testSwing.getAcceleratorYData();

        final int result = swingSearchService.searchContinuityAboveValueTwoSignals(
                testData1,
                testData2,
                0,
                100,
                0,
                1.2,
                10
        );

        assertEquals(-1, result);
    }

    @Test
    void searchMultiContinuityWithinRange_thresholdZero_threeFound() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int[][] result = swingSearchService.searchMultiContinuityWithinRange(
                testData,
                0,
                23,
                0,
                999,
                1
        );

        assertArrayEquals(new int[][]{new int[]{20, 20}, new int[]{21, 21}, new int[]{22, 22}}, result);
    }

    @Test
    void searchMultiContinuityWithinRange_thresholdNegative_threeFound() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int[][] result = swingSearchService.searchMultiContinuityWithinRange(
                testData,
                0,
                7,
                -1,
                999,
                1
        );

        assertArrayEquals(new int[][]{new int[]{4, 4}, new int[]{5, 5}, new int[]{6, 6}}, result);
    }

    @Test
    void searchMultiContinuityWithinRange_thresholdPositive_threeFound() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int[][] result = swingSearchService.searchMultiContinuityWithinRange(
                testData,
                0,
                40,
                1,
                999,
                1
        );

        assertArrayEquals(new int[][]{new int[]{37, 37}, new int[]{38, 38}, new int[]{39, 39}}, result);
    }

    @Test
    void searchMultiContinuityWithinRange_thresholdDouble_threeFound() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int[][] result = swingSearchService.searchMultiContinuityWithinRange(
                testData,
                0,
                42,
                1.1,
                999,
                1
        );

        assertArrayEquals(new int[][]{new int[]{39, 39}, new int[]{40, 40}, new int[]{41, 41}}, result);
    }

    @Test
    void searchMultiContinuityWithinRange_noMatches_noneFound() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int[][] result = swingSearchService.searchMultiContinuityWithinRange(
                testData,
                0,
                testData.length,
                999,
                999,
                1
        );

        assertArrayEquals(new int[][]{}, result);
    }

    @Test
    void searchMultiContinuityWithinRange_winLengthTen_threeFound() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int[][] result = swingSearchService.searchMultiContinuityWithinRange(
                testData,
                0,
                32,
                0,
                999,
                10
        );

        assertArrayEquals(new int[][]{new int[]{20, 29}, new int[]{21, 30}, new int[]{22, 31}}, result);
    }

    @Test
    void searchMultiContinuityWithinRange_winLengthIsNotMet_noneFound() {

        final double[] testData = testSwing.getAcceleratorXData();

        final int[][] result = swingSearchService.searchMultiContinuityWithinRange(
                testData,
                0,
                100,
                1.4,
                999,
                10
        );

        assertArrayEquals(new int[][]{}, result);
    }
}
