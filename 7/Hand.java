import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Hand {
    public int bid;
    public String cards;

    /**
     * Hand types:
     * Five of a kind: 7
     * Four of a kind: 6
     * Full house: 5
     * Three of a kind: 4
     * Two pair: 3
     * One pair: 2
     * High card: 1
     */
    public int type;

    /**
     * 1 = 1
     * 2 = 2
     * 3 = 3
     * 4 = 4
     * 5 = 5
     * 6 = 6
     * 7 = 7
     * 8 = 8
     * 9 = 9
     * T = 10
     * J = 11
     * Q = 12
     * K = 13
     * A = 14
     */
    public int[] cardValues;


    public Hand(String line) {
        String[] splitLine = line.split(" ");
        cards = splitLine[0];
        bid = Integer.parseInt(splitLine[1]);
        determineType();
        determineValues();
    }

    public void determineType() {
        // count the occurances of each card
        HashMap<Character, Integer> charCounts = new HashMap<>();
        
        for (int i = 0; i < 5; i++) {
            char ch = this.cards.charAt(i);
            Integer count = charCounts.get(ch) == null ? 0 : charCounts.get(ch);
            charCounts.put(ch, count + 1);
        }

        ArrayList<Integer> counts = new ArrayList<>(charCounts.values());
        // sort greatest count first
        counts.sort(Comparator.reverseOrder());

        // figure out the type based on the card counts
        switch (counts.get(0)) {
            case 5:
                type = 7;
                break;
            case 4:
                type = 6;
                break;
            case 3:
                type = counts.get(1) == 2 ? 5 : 4;
                break;
            case 2:
                type = counts.get(1) == 2 ? 3 : 2;
                break;
            default:
                type = 1;
                break;
        }
    }

    public void determineValues() {
        cardValues = new int[5];
        for (int i = 0; i < 5; i++) {
            String card = String.valueOf(this.cards.charAt(i));
            switch (card) {
                case "T":
                    cardValues[i] = 10;
                    break;
                case "J":
                    cardValues[i] = 11;
                    break;
                case "Q":
                    cardValues[i] = 12;
                    break;
                case "K":
                    cardValues[i] = 13;
                    break;
                case "A":
                    cardValues[i] = 14;
                    break;
                default:
                    cardValues[i] = Integer.parseInt(card);
                    break;
            }
        }
    }
}
