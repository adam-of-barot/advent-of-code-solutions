import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * CubeConundrum #1
 * 
 * Colored cubes are put in a bag.
 * Cubes are drawn from the bag at randoom, shown, and then placed back.
 * This is done multiple times.
 * Determine which games (lines) from the input list are possible for the following configuration:
 * 12 red cubes, 13 green cubes, and 14 blue cubes are in the bag.
 */
public class CubeConundrum1 {

    public static void main(String[] args) {
        // possible max values
        HashMap<String, Integer> colorMap = new HashMap<String, Integer>();
        colorMap.put("red", 12);
        colorMap.put("green", 13);
        colorMap.put("blue", 14);

        // read input file
        try (BufferedReader reader = new BufferedReader(new FileReader("./input.txt"))) {
            String line;
            Integer idx = 1;
            Integer sum = 0;
            while ((line = reader.readLine()) != null) {
                Boolean possible = true;
                // remove "Game X: " from string
                String[] line_split = line.split("Game \\d+: ");
                // split further
                String[] cubes = line_split[1].split("[,;] ");
                // loop through results cubes drawn
                for (String cube : cubes) {
                    // split number and color
                    String[] numberAndColor = cube.split(" ");
                    Integer num = Integer.parseInt(numberAndColor[0]);
                    String color = numberAndColor[1];
                    Integer numMax = colorMap.get(color);

                    // test if number of cubes drawn is greater than max possible value
                    if (num > numMax) {
                        possible = false;
                        System.out.println("Game " + idx + " is impossible");
                        System.out.println(num + " " + color + " vs " + numMax);
                        break;
                    }
                }

                // record line number if game is possible
                if (possible) {
                    sum += idx;
                }

                idx++;
            }
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}