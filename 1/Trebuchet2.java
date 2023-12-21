/**
 * Trebuchet #2
 * 
 * Given an input file, get the first and last digits of each line.
 * Strings that spell out a number (ex: "one") also count as valid digits.
 * Convert those digits to a number.
 * Return the sum of all numbers.
 */
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
public class Trebuchet2 {

    public static void main(String[] args) {
        // these strings count as digits
        HashMap<String, Integer> digitMap = new HashMap<String, Integer>();
        digitMap.put("one", 1);
        digitMap.put("two", 2);
        digitMap.put("three", 3);
        digitMap.put("four", 4);
        digitMap.put("five", 5);
        digitMap.put("six", 6);
        digitMap.put("seven", 7);
        digitMap.put("eight", 8);
        digitMap.put("nine", 9);
        // read in input file
        try (BufferedReader reader = new BufferedReader(new FileReader("./input.txt"))) {
            // parse each line twice
            // first pass through: try to convert every char to an integer
            // second pass through: check for spelled out digits
            String line;
            Integer sum = 0;
            Integer idx = 1;
            while ((line = reader.readLine()) != null) {
                char[] chars = {0,0};
                int[] indexes = {0,0};
                int currentIndex = -1;
                Boolean checkedFirst = false;
                // first pass
                for (char ch : line.toCharArray()) {
                    currentIndex++;
                    if (!Character.isDigit(ch)) {
                        continue;
                    }
                    if (!checkedFirst) {
                        chars[0] = ch;
                        indexes[0] = currentIndex;
                        checkedFirst = true;
                    }
                    chars[1] = ch;
                    indexes[1] = currentIndex;
                }
                // second pass
                for (Map.Entry<String, Integer> entry : digitMap.entrySet()) {
                    String key = entry.getKey();
                    int firstIndex = line.indexOf(key);
                    int lastIndex = line.lastIndexOf(key);
                    if (firstIndex < 0 && lastIndex < 0) {
                        continue;
                    }
                    Character value = Character.forDigit(entry.getValue(), 10);
                    if (firstIndex <= indexes[0]) {
                        chars[0] = value;
                        indexes[0] = firstIndex;
                    }
                    if (lastIndex > indexes[1]) {
                        chars[1] = value;
                        indexes[1] = lastIndex;
                    }
                }
                String digits = new String(chars);
                System.out.println(idx + " " + digits);
                try {
                    sum += Integer.parseInt(digits);
                } catch (NumberFormatException e) {
                    System.out.println("Unable too convert digits to integer: " + digits);
                }
                idx++;
            }
            System.out.println(sum);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error handling file");
            e.printStackTrace();
        }
    }
}