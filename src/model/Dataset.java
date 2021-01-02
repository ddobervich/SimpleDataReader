package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/***
 * A limited CSV reader to provide ArrayLists of columns or rows for students to practice using.
 *
 * Note that this reader will NOT correctly parse elements that have the separating character within a quoted string.
 * For example, if it's a standard comma separated value file, it will NOT parse...
 *
 * 01, David Dobervich, "well, this won't work", 40, email@gmail.com
 */
public class Dataset {
    private static final String DEFAULT_SEPARATOR = ",";

    private ArrayList<ArrayList<String>> columnData;
    private ArrayList<String> columnNames;
    private int numCols, numRows;
    private String filePath;
    private String separator;

    private Dataset() {
        columnNames = new ArrayList<>();
        columnData = new ArrayList<>();
    }

    /***
     * Get a Dataset object from a comma separated value (csv) file.  This method will use the first row as the
     * column names.
     * @param filePath csv file to parse
     * @return a Dataset object with the contents of the file
     */
    public static Dataset makeDatasetFromFile(String filePath) {
        return makeDatasetFromFile(filePath, DEFAULT_SEPARATOR, true);
    }

    /***
     * Get a Dataset object from a comma separated value (csv) file.
     * @param filePath csv file to parse
     * @param separator what character (or string) to use as a the separating character (comma, tab, semi-colon, etc.).
     * @param hasHeaderRow whether or not to use the first row as the column names
     * @return a Dataset object with the contents of the file
     */
    public static Dataset makeDatasetFromFile(String filePath, String separator, boolean hasHeaderRow) {
        Dataset results = new Dataset();
        results.filePath = filePath;
        results.separator = separator;

        try (Scanner scanner = new Scanner(new File(filePath))) {

            // === process first row ===
            String firstLine = scanner.nextLine();
            String[] firstRowData = firstLine.split(separator);
            results.numCols = firstRowData.length;
            initEmptyColumnLists(results);

            int row = 0;
            if (hasHeaderRow) {
                for (String name : firstRowData) {
                    results.columnNames.add(name.trim());
                }
            } else {
                results.columnNames = makeGenericHeaderNames(results.numCols);
                processLine(firstLine, row, results, separator);
            }

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                row++;
                processLine(line, row, results, separator);
            }

            results.numRows = row;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    /***
     * If no header names are provided in the file, create generic ones called "Column 0", "Column 1", etc.
     * @param numCols number of columns to make names for
     * @return list of names
     */
    private static ArrayList<String> makeGenericHeaderNames(int numCols) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            names.add("Column" + i);
        }
        return names;
    }

    private static void initEmptyColumnLists(Dataset results) {
        for (int i = 0; i < results.numCols; i++) {
            ArrayList<String> col = new ArrayList<>();
            results.columnData.add(col);
        }
    }

    private static void processLine(String line, int rowNum, Dataset results, String separator) {
        String[] rowData = line.split(separator);

        if (rowData.length != results.numCols) {
            System.err.println("Warning: row " + rowNum + " only has " + rowData.length
                    + " columns, but should have " + results.numCols);
            System.err.println("--== SKIPPING LINE ==--");
            return;
        }

        for (int i = 0; i < rowData.length; i++) {
            ArrayList<String> col = results.columnData.get(i);
            col.add(rowData[i]);
        }
    }

    /***
     * @return number of rows in the data set.
     */
    public int numRows() {
        return this.numRows;
    }

    /***
     * @return number of columns in the data set.
     */
    public int numCols() {
        return this.numCols;
    }

    /***
     * @return ArrayList of the names of all the columns
     */
    public ArrayList<String> getColumnNames() {
        return new ArrayList<String>(Collections.unmodifiableList(this.columnNames));   // TODO: does this even make sense?
    }

    /***
     * Display some summary information about the data set.  The file path it was loaded from, the # of rows and
     * columns, and the first 5 rows including the column names.
     */
    public void summarize() {
        System.out.println("Data from: " + filePath);
        System.out.println("# cols: " + numCols + " # rows: "+ numRows);
        System.out.println("----------------------------------------------");
        System.out.println("First 5 rows (including headers)");
        System.out.println();
        System.out.println("Column names:\n" + this.columnNames);
        printFirstNRows(5);
        System.out.println("----------------------------------------------");
        System.out.println();
    }

    /***
     * Display the first n rows of the dataset
     * @param n number of rows to display
     */
    public void printFirstNRows(int n) {
        n = Math.min(n, this.numRows);
        for (int i = 0; i < n; i++) {
            printRow(i);
        }
    }

    /***
     * display row i from the data
     * @param i the row to display
     */
    public void printRow(int i) {
        if (i < 0 || i >= this.numRows) return;

        for (int j = 0; j < columnData.size()-1; j++) {
            ArrayList<String> col = columnData.get(j);
            System.out.print(col.get(i) + this.separator);
        }

        ArrayList<String> col = columnData.get(columnData.size()-1);
        System.out.println(col.get(i));
    }

    /***
     * Return column x as an ArrayList of Strings
     * @param x the column number to fetch
     * @return the column of data as Strings
     */
    public ArrayList<String> getStringCol(int x) {
        if (x < 0 || x >= columnData.size()) {
            System.err.println(x + " is not a valid column.  Run summarize() to see columns.");
            return null;
        }

        return columnData.get(x);
    }

    /***
     * Return column x as an ArrayList of Doubles.  If any values cannot be read as doubles, an error message is
     * displayed and method returns null.
     * @param x column number to get
     * @return list of column values as Doubles
     */
    public ArrayList<Double> getDoubleCol(int x) {
        ArrayList<String> col = getStringCol(x);
        if (col == null) return null;

        return convertToDouble(col);
    }

    private ArrayList<Double> convertToDouble(ArrayList<String> col) {
        if (col == null || col.size() == 0) return new ArrayList<Double>();
        ArrayList<Double> out = new ArrayList<>();

        for (int i = 0; i < col.size(); i++) {
            String val = col.get(i);
            try {
                Double d = Double.parseDouble(val.trim());
                out.add(d);
            } catch (Exception e) {
                System.err.println("Error: Cannot convert row " + i + " value '" + val + "' to a double");
            }
        }

        if (out.size() != col.size()) return null;      // because there were errors with converting
        return out;
    }

    /***
     * Return column x as an ArrayList of Integers.  If any values cannot be read as integers, an error message is
     * displayed and method returns null.
     * @param x column number to get
     * @return list of column values as Integers
     */
    public ArrayList<Integer> getIntCol(int x) {
        ArrayList<String> col = getStringCol(x);
        if (col == null) return null;

        return convertToInt(col);
    }

    private ArrayList<Integer> convertToInt(ArrayList<String> col) {
        if (col == null || col.size() == 0) return new ArrayList<Integer>();
        ArrayList<Integer> out = new ArrayList<>();

        for (int i = 0; i < col.size(); i++) {
            String val = col.get(i);
            try {
                Integer d = Integer.parseInt(val.trim());
                out.add(d);
            } catch (Exception e) {
                System.err.println("Error: Cannot convert row " + i + " value '" + val + "' to an integer");
            }
        }

        if (out.size() != col.size()) return null;      // because there were errors with converting
        return out;
    }

    /***
     * Return column x as an ArrayList of Floats.  If any values cannot be read as floats, an error message is
     * displayed and method returns null.
     * @param x column number to get
     * @return list of column values as Doubles
     */
    public ArrayList<Float> getFloatCol(int x) {
        ArrayList<String> col = getStringCol(x);
        if (col == null) return null;

        return convertToFloat(col);
    }


    private ArrayList<Float> convertToFloat(ArrayList<String> col) {
        if (col == null || col.size() == 0) return new ArrayList<Float>();
        ArrayList<Float> out = new ArrayList<>();

        for (int i = 0; i < col.size(); i++) {
            String val = col.get(i);
            try {
                Float d = Float.parseFloat(val.trim());
                out.add(d);
            } catch (Exception e) {
                System.err.println("Error: Cannot convert row " + i + " value '" + val + "' to a float");
            }
        }

        if (out.size() != col.size()) return null;      // because there were errors with converting
        return out;
    }

    /***
     * Return column x as an ArrayList of Booleans.  If any values cannot be read as booleans, an error message is
     * displayed and method returns null.
     * @param x column number to get
     * @return list of column values as Booleans
     */
    public ArrayList<Boolean> getBooleanCol(int x) {
        ArrayList<String> col = getStringCol(x);
        if (col == null) return null;

        return convertToBoolean(col);
    }

    private ArrayList<Boolean> convertToBoolean(ArrayList<String> col) {
        if (col == null || col.size() == 0) return new ArrayList<Boolean>();
        ArrayList<Boolean> out = new ArrayList<>();

        for (int i = 0; i < col.size(); i++) {
            String val = col.get(i);
            try {
                Boolean d = Boolean.parseBoolean(val.trim());
                out.add(d);
            } catch (Exception e) {
                System.err.println("Error: Cannot convert row " + i + " value '" + val + "' to an integer");
            }
        }

        if (out.size() != col.size()) return null;      // because there were errors with converting
        return out;
    }

    /***
     * return a ArrayList of Strings for all the values in row i.  If row number isn't valid an error is displayed and
     * method returns null.
     * @param i row number to fetch
     * @return List of strings for the values in row i
     */
    public ArrayList<String> getRow(int i) {
        if (i < 0 || i >= numRows) {
            System.err.println(i + " is not a valid row.  Run summarize() to see total # of rows.");
            return null;
        }
        ArrayList<String> row = new ArrayList<>();

        for (int j = 0; j < columnData.size(); j++) {
            ArrayList<String> column = columnData.get(j);
            row.add(column.get(i));
        }

        return row;
    }
}