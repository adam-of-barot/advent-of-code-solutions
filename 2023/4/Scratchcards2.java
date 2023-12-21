import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
/**
 * Scratchcards Part #2
 * 
 */
public class Scratchcards2 {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./input.txt"))) {
            String line;
            Integer idx = 0;
            int[] instances = new int[213];
            while ((line = reader.readLine()) != null) {
                // skip "Card X:" stuff
                line = line.substring(10);

                // parts[0] = winning numbers
                // parts[1] = numbers that I have
                String[] parts = line.split("\\|");
                HashSet<Integer> winningNums = new HashSet<Integer>();
                HashSet<Integer> myNums = new HashSet<Integer>();

                for (String str : parts[0].strip().split("\\s+")) {
                    winningNums.add(Integer.parseInt(str));
                }

                for (String str : parts[1].strip().split("\\s+")) {
                    myNums.add(Integer.parseInt(str));
                }

                myNums.retainAll(winningNums);
                int count = myNums.size();

                instances[idx] += 1;

                for (int s = 1; s <= count; s++) {
                    try {
                        instances[idx + s] += instances[idx];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // do nothing
                    }
                }

                idx++;
            }
            Integer instanceSum = Arrays.stream(instances).reduce(0, (subtotal, element) -> subtotal + element);
            System.out.println(instanceSum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}