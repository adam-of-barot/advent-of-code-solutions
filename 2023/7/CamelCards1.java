import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * CamelCards Part #1
 * 
 * Order the input lines by hand strength (weakest gets Rank 1).
 * Multiply rank by bid amount.
 * Sum up results of multiplication.
 */
public class CamelCards1 {

    HashMap<String, Integer> cardValueMap = new HashMap<String, Integer>();

    public static ArrayList<Hand> loadData(String fileName) {
        ArrayList<Hand> hands = new ArrayList<Hand>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Hand hand = new Hand(line);
                hands.add(hand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        hands.sort(new Comparator<Hand>() {
            @Override
            public int compare(Hand first, Hand second) {
                // primary sort: by type
                int typeCompare = Integer.compare(first.type, second.type);
                if (typeCompare != 0) {
                    return typeCompare;
                }
                // secondary sort: by card values
                int valCompare = 0;
                for (int i = 0; i < 5; i++) {
                    int firstVal = first.cardValues[i];
                    int secondVal = second.cardValues[i];
                    int result = Integer.compare(firstVal, secondVal);
                    if (result != 0) {
                        valCompare = result;
                        break;
                    }
                }
                return valCompare;
            }
        });
        return hands;
    }

    public static void main(String[] args) {  
        ArrayList<Hand> hands = loadData("./input.txt");
        int sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            Hand h = hands.get(i);
            sum += h.bid * (i + 1);
        }
        System.out.println(sum);
    }
}