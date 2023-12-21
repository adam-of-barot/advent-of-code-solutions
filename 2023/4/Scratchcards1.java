import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
/**
 * Scratchcards Part #1
 * 
 * Find how many point is a card worth.
 * Left side of input line: winning numbers.
 * Right side of input line: my numbers.
 * Card is worth points if winning numbers contain one or more of my numbers.
 * 1 match -> 1 point
 * every additional match -> double previous point value (1 -> 2 -> 4 etc)
 * Sum up point values of all cards.
 */
public class Scratchcards1 {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./input.txt"))) {
            String line;
            Integer sum = 0;
            while ((line = reader.readLine()) != null) {
                // skip "Card X:" stuff
                line = line.substring(10);

                // parts[0] = winning cards
                // parts[1] = cards that I have
                String[] parts = line.split("\\|");
                HashSet<Integer> winningNums = new HashSet<Integer>();
                HashSet<Integer> myNums = new HashSet<Integer>();

                for (String str : parts[0].strip().split("\\s+")) {
                    winningNums.add(Integer.parseInt(str));
                }

                for (String str : parts[1].strip().split("\\s+")) {
                    myNums.add(Integer.parseInt(str));
                }

                Integer score = 0;
                for (Integer myNum : myNums) {
                    if (winningNums.contains(myNum)) {
                        if (score == 0) {
                            score = 1;
                        } else {
                            score *= 2;
                        }
                    }
                }
                sum += score;
            }
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}