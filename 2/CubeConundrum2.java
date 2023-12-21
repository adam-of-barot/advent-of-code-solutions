import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * CubeConundrum #1
 * 
 * Colored cubes are put in a bag.
 * Cubes are drawn from the bag at randoom, shown, and then placed back.
 * This is done multiple times.
 * Determine the minimum number of cubes of each color for a game to be possible.
 * Multiply these minimum cube numbers to get the power of the game.
 * Sum up the powers.
 */
public class CubeConundrum2 {

    public static void main(String[] args) {
        // read input file
        try (BufferedReader reader = new BufferedReader(new FileReader("./input.txt"))) {
            String line;
            Integer sum = 0;
            while ((line = reader.readLine()) != null) {
                // remove "Game X: " from string
                String[] line_split = line.split("Game \\d+: ");
                // split further
                String[] cubes = line_split[1].split("[,;] ");
                HashMap<String, Integer> lineMap = new HashMap<String, Integer>();
                // loop through results cubes drawn
                for (String cube : cubes) {
                    // split number and color
                    String[] numberAndColor = cube.split(" ");
                    Integer num = Integer.parseInt(numberAndColor[0]);
                    String color = numberAndColor[1];
                    Integer minValue = lineMap.get(color);
                    
                    if (minValue == null || num > minValue) {
                        lineMap.put(color, num);
                    }
                }

                sum += lineMap.values().stream().reduce(1, (subtotal, element) -> subtotal * element);
            }
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}