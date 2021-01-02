import model.Dataset;

import java.util.ArrayList;

public class DatasetExamples {
    public static void main(String[] args) {
        Dataset results = Dataset.makeDatasetFromFile("data/happiness.csv");

        results.summarize();

        int rows = results.numRows();
        int cols = results.numCols();
        ArrayList<String> colNames = results.getColumnNames();

        ArrayList<String> countries = results.getStringCol(0);
        ArrayList<String> genders = results.getStringCol(1);
        ArrayList<Double> averages = results.getDoubleCol(2);
        ArrayList<Integer> sampleSizes = results.getIntCol(3);

        ArrayList<String> row = results.getRow(3);
        System.out.println(row);
    }
}