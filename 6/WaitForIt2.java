import java.io.BufferedReader;
import java.io.FileReader;

/**
 * WaitForIt Part #2
 */
public class WaitForIt2 {

    public static void main(String[] args) {
        Long[] data = loadData("./input.txt");
        Long time = data[0];
        Long distance = data[1];

        int waysToWin = 0;
        for (int t = 1; t < time; t++) {
            long distanceTraveled = t*(time - t);
            if (distanceTraveled > distance) {
                waysToWin++;
            }
        }

        System.out.println(waysToWin);
        
    }

    public static Long[] loadData(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String timeLine = reader.readLine().substring(5).replace(" ", "");
            String distLine = reader.readLine().substring(9).replace(" ", "");

            Long time = Long.parseLong(timeLine);
            Long dist = Long.parseLong(distLine);

            return new Long[]{time, dist};

        } catch (Exception e) {
            e.printStackTrace();
            return new Long[]{};
        }
    }
}