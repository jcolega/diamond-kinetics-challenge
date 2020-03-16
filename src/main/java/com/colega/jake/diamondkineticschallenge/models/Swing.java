package com.colega.jake.diamondkineticschallenge.models;

import com.colega.jake.diamondkineticschallenge.exceptions.SwingDataNotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

public class Swing {

    private RealMatrix data;

    public Swing(final String string) {
        try {
            this.initializeData(CSVParser.parse(string, CSVFormat.DEFAULT));
        } catch (final IOException e) {
            throw new SwingDataNotFoundException(e);
        }
    }

    public Swing(final File file) {
        try {
            this.initializeData(CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT));
        } catch (final IOException e) {
            throw new SwingDataNotFoundException(e);
        }
    }

    public Swing(final Path path) {
        try {
            this.initializeData(CSVParser.parse(path, Charset.defaultCharset(), CSVFormat.DEFAULT));
        } catch (final IOException e) {
            throw new SwingDataNotFoundException(e);
        }
    }

    public Swing(final InputStream inputStream) {
        try {
            this.initializeData(CSVParser.parse(inputStream, Charset.defaultCharset(), CSVFormat.DEFAULT));
        } catch (final IOException e) {
            throw new SwingDataNotFoundException(e);
        }
    }

    private void initializeData(final CSVParser csvParser) {

        final List<CSVRecord> csvRecordList;
        try {
            csvRecordList = csvParser.getRecords();
        } catch (final IOException e) {
            throw new SwingDataNotFoundException(e);
        }

        final double[][] rawData = new double[csvRecordList.size()][];

        int i = 0;
        for (final CSVRecord csvRecord : csvRecordList) {

            rawData[i] = new double[]{
                    Double.parseDouble(csvRecord.get(0)),
                    Double.parseDouble(csvRecord.get(1)),
                    Double.parseDouble(csvRecord.get(2)),
                    Double.parseDouble(csvRecord.get(3)),
                    Double.parseDouble(csvRecord.get(4)),
                    Double.parseDouble(csvRecord.get(5)),
                    Double.parseDouble(csvRecord.get(6))
            };

            i++;
        }

        this.data = new Array2DRowRealMatrix(rawData);
    }

    public double[] getTimeData() {
        return this.data.getColumn(0);
    }

    public double[] getAcceleratorXData() {
        return this.data.getColumn(1);
    }

    public double[] getAcceleratorYData() {
        return this.data.getColumn(2);
    }

    public double[] getAcceleratorZData() {
        return this.data.getColumn(3);
    }

    public double[] getGyroscropeXData() {
        return this.data.getColumn(4);
    }

    public double[] getGyroscropeYData() {
        return this.data.getColumn(5);
    }

    public double[] getGyroscropeZData() {
        return this.data.getColumn(6);
    }
}
