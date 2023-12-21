import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

/**
 * WaitForIt Part #1
 */
public class WaitForIt1 {

    public static void main(String[] args) {
        Integer[][] data = loadData("./input.txt");
        Integer[] times = data[0];
        Integer[] distances = data[1];

        int totalWins = 1;

        for (int i = 0; i < times.length; i++) {
            int waysToWin = 0;
            for (int t = 1; t < times[i]; t++) {
                int distanceTraveled = t*(times[i] - t);
                if (distanceTraveled > distances[i]) {
                    waysToWin++;
                }
            }
            totalWins *= waysToWin;
        }

        System.out.println(totalWins);
        
    }

    public static Integer[][] loadData(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String pattern = "\\s+";
            String[] timeLine = reader.readLine().split(pattern);
            String[] distLine = reader.readLine().split(pattern);

            Integer[] times = Arrays.stream(timeLine, 1, timeLine.length).map(str -> Integer.parseInt(str)).toArray(Integer[]::new);
            Integer[] distances = Arrays.stream(distLine, 1, distLine.length).map(str -> Integer.parseInt(str)).toArray(Integer[]::new);

            return new Integer[][]{times, distances};

        } catch (Exception e) {
            e.printStackTrace();
            return new Integer[][]{};
        }
    }
}