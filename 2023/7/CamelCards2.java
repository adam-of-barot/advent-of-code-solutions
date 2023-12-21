import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * CamelCards Part #2
 * 
 * Order the input lines by hand strength (weakest gets Rank 1).
 * Multiply rank by bid amount.
 * Sum up results of multiplication.
 * 
 * J is now a joker card.
 * It has a lower value than the 2 card.
 * It morphs into the appropriate card to get the strongest hand type possible.
 */
public class CamelCards2 {

    HashMap<String, Integer> cardValueMap = new HashMap<String, Integer>();

    public static ArrayList<HandJoker> loadData(String fileName) {
        ArrayList<HandJoker> hands = new ArrayList<HandJoker>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                HandJoker hand = new HandJoker(line);
                hands.add(hand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        hands.sort(new Comparator<HandJoker>() {
            @Override
            public int compare(HandJoker first, HandJoker second) {
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
        ArrayList<HandJoker> hands = loadData("./input.txt");
        int sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            HandJoker h = hands.get(i);
            sum += h.bid * (i + 1);
        }
        System.out.println(sum);
    }
}