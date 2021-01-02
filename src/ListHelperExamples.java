import model.ListHelper;

import java.util.ArrayList;
import java.util.List;

public class ListHelperExamples {
    public static void main(String[] args) {

        // ---=== Make ArrayLists from comma separated inputs ===---
        ArrayList<Integer> vals = ListHelper.makeList(1, 6, 3, 2, 6, 7, 1, 3, 2);
        ArrayList<String> letters = ListHelper.makeList("a", "b", "x", "q", "w");

        // ---=== Get lines from a url as ArrayList of Strings ===---
        String adjs = "https://raw.githubusercontent.com/dominictarr/random-name/master/first-names.txt";
        ArrayList<String> names = ListHelper.readUrlAsList(adjs);

        // ---=== Get lines from a file as ArrayList of Strings ===---
        ArrayList<String> classList = ListHelper.readFileAsList("data/names.txt");

    }
}
