import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
//import java.util.Arrays;

public class Universe {

    private ArrayList<ArrayList<String>> rawData = new ArrayList<>();
    public ArrayList<Galaxy> galaxies = new ArrayList<>();
    private int expansionFactor;

    public Universe(String fileName, int expansionFactor) {

        this.expansionFactor = expansionFactor - 1;

        // keep track of empty column indexes
        ArrayList<Integer> emptyColumns = new ArrayList<>();
        ArrayList<Integer> emptyRows = new ArrayList<>();
        boolean initEmptyColumns = true;

        // load raw data

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            int rowIdx = 0;

            while ((line = reader.readLine()) != null) {
                char[] chars = line.toCharArray();

                if (!line.contains("#")) {
                    emptyRows.add(rowIdx);
                }

                ArrayList<String> strings = new ArrayList<>();
                Integer col = 0;
                for (char ch : chars) {

                    String str = String.valueOf(ch);

                    if (initEmptyColumns && str.equals(".")) {
                        emptyColumns.add(col);
                    } else if (!initEmptyColumns && str.equals("#")) {
                        emptyColumns.remove(col);
                    }

                    strings.add(str);
                    col++;
                }
                rawData.add(strings);
                initEmptyColumns = false;
                rowIdx++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // expand universe + record galaxy coordinates

        int galaxyCount = 1;
        int rowIdx = 0;
        int expRowIdx = 0;
        for (ArrayList<String> row : rawData) {
            // expand universe row wise
            if (emptyRows.contains(rowIdx)) {
                expRowIdx += this.expansionFactor;
            }
            // record galaxy coordinates
            int colIdx = 0;
            int expColIdx = 0;
            for (String str : row) {
                // expand universe column wise
                if (emptyColumns.contains(colIdx)) {
                    expColIdx += this.expansionFactor;
                }
                if (str.equals("#")) {
                    Galaxy galaxy = new Galaxy(galaxyCount, expRowIdx, expColIdx);
                    galaxies.add(galaxy);
                    galaxyCount++;
                }
                colIdx++;
                expColIdx++;
            }
            rowIdx++;
            expRowIdx++;
        }

        //System.out.println(Arrays.deepToString(data.toArray()));
    }
}
