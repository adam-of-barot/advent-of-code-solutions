import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * GearRatios Part #1
 * Sum up all the numbers in the input that are adjacent to a symbol.
 * The dot character does not count as a symbol.
 */
public class GearRatios1 {

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
        HashSet<Integer> keys = new HashSet<Integer>();
        for (int[] coord : symbolCoords) {
            int row = coord[0];
            int col = coord[1];
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    try {
                        Integer key = arr[i][j];
                        if (key == 0) {
                            continue;
                        }
                        keys.add(key);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        
                    }
                }
            }
        }

        // create sum by swapping key to value from HashSet, and adding them together
        Integer sum = keys.stream().map(key -> numberMap.get(key)).reduce(0, (subtotal, element) -> subtotal + element);
        System.out.println(sum);
    }
}