import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * GearRatios Part #2
 * 
 * Find all symbols that are next to exactly two numbers.
 * Multiply said numbers.
 * Sum up multiplication results.
 */
public class GearRatios2 {

    public static void main(String[] args) {
        // array dimensions: 140 x 140
        int[][] arr = new int[140][140];
        // use this to store symbol coordinates
        ArrayList<int[]> symbolCoords = new ArrayList<int[]>();
        // map used to store numbers with unique values
        Integer mapIdx = 1;
        HashMap<Integer, Integer> numberMap = new HashMap<Integer, Integer>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./input.txt"))) {
            // load the data into the array
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                char[] chars = line.toCharArray();
                String buffer = "";
                for (int col = 0; col < chars.length; col++) {
                    // check current char, add to buffer if integer
                    char ch = chars[col];
                    if (Character.isDigit(ch)) {
                        buffer += ch;
                        arr[row][col] = mapIdx;
                    } else if (!Character.toString(ch).equals(".")) {
                        // if symbol, add it to other array
                        int[] a = {row, col};
                        symbolCoords.add(a);
                    }
                    // flush buffer if next is not number, or end of line
                    try {
                        Character next = chars[col+1];
                        if (!Character.isDigit(next)) {
                            try {
                                Integer num = Integer.parseInt(buffer);
                                numberMap.put(mapIdx, num);
                                mapIdx++;
                                buffer = "";
                            } catch (NumberFormatException e) {
                            }
                        }                      
                    } catch (ArrayIndexOutOfBoundsException e) {
                        try {
                            Integer num = Integer.parseInt(buffer);
                            numberMap.put(mapIdx, num);
                            mapIdx++;
                            buffer = "";
                        } catch (NumberFormatException e2) {
                        }
                    }
                }
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // collect keys of the numbers that are adjacent to the symbol coords
        Integer sum = 0;
        for (int[] coord : symbolCoords) {
            int row = coord[0];
            int col = coord[1];
            HashSet<Integer> keys = new HashSet<Integer>();
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    try {
                        Integer key = arr[i][j];
                        if (key == 0) {
                            continue;
                        }
                        keys.add(key);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // do nothing
                    }
                }
            }
            if (keys.size() == 2) {
                sum += keys.stream().map(key -> numberMap.get(key)).reduce(1, (mult, element) -> mult * element);
            }
        }
        System.out.println(sum);
    }
}