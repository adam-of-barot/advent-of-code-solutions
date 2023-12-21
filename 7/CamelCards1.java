import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
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
        return hands;
    }

    public static void main(String[] args) {  
        ArrayList<Hand> hands = loadData("./example.txt");
        for (Hand h : hands) {
            String str = String.format("Cards: %s, Type: %d", h.cards, h.type);
            System.out.println(str);
        }
    }
}