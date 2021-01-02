package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/***
 * A helper class for students learning about ArrayList for the first time.  Provides static methods for reading files
 * and URLs (of raw text files) as ArrayLists.
 */
public class ListHelper {

    /***
     * Return an ArrayList of Strings, one per line of the input file.
     * @param filePath the file path of the text file to read
     * @return and ArrayList of Strings consisting of each line of the file
     */
    public static ArrayList<String> readFileAsList(String filePath) {
        ArrayList<String> output = new ArrayList<>();

           try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                output.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    /***
     * Return the first numLines lines of the url pointed to by urlString.  To get the entire file use the other
     * version without specifiying numLines.  This is for testing purposes if you are accessing a large file and you
     * don't want to download the whole thing each time you run the code.
     *
     * @param urlString the url string to fetch
     * @return the first numLines lines of the url pointed to by urlString.
     */
    public static ArrayList<String> readUrlAsList(String urlString, int numLines) {
        ArrayList<String> output = new ArrayList<>();

        try {
            int currentLine = 0;
            URL url = new URL(urlString);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null && currentLine < numLines) {
                output.add(line);
                currentLine++;
            }
            in.close();
        }
        catch (MalformedURLException e) {
            System.out.println("This is not a valid URL string: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("There was an error reading the url: " + e.getMessage());
        }

        return output;
    }

    /***
     * Return an ArrayList of lines of the file pointed to by the urlString.
     *
     * @param urlString the url string to fetch
     * @return an ArrayList of lines of the file pointed to by the urlString.
     */
    public static ArrayList<String> readUrlAsList(String urlString) {
        ArrayList<String> output = new ArrayList<>();

        try {
            URL url = new URL(urlString);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                output.add(line);
            }
            in.close();
        }
        catch (MalformedURLException e) {
            System.out.println("This is not a valid URL string: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("There was an error reading the url: " + e.getMessage());
        }

        return output;
    }

    /***
     * Return ArrayList of input strings.
     * @param in comma separated list of strings to make a list out of
     * @return an ArrayList of input strings.
     */
    public static ArrayList<String> makeList(String...in) {
        List<String> list = Arrays.asList(in);
        return new ArrayList<String>(list);
    }

    /***
     * Return ArrayList of Doubles
     * @param in comma separated list of Doubles to make a list out of
     * @return an ArrayList of Doubles.
     */
    public static ArrayList<Double> makeList(Double...in) {
        List<Double> list = Arrays.asList(in);
        return new ArrayList<Double>(list);
    }

    /***
     * Return ArrayList of input Booleans.
     * @param in comma separated list of Booleans to make a list out of
     * @return an ArrayList of Booleans.
     */
    public static ArrayList<Boolean> makeList(Boolean...in) {
        List<Boolean> list = Arrays.asList(in);
        return new ArrayList<Boolean>(list);
    }

    /***
     * Return ArrayList of input Integers.
     * @param in comma separated list of Integers to make a list out of
     * @return an ArrayList of Integers.
     */
    public static ArrayList<Integer> makeList(Integer...in) {
        List<Integer> list = Arrays.asList(in);
        return new ArrayList<Integer>(list);
    }
}