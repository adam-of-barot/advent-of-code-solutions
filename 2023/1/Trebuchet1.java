/**
 * Trebuchet #1
 * 
 * Given an input file, get the first and last digits of each line.
 * Convert those digits to a number.
 * Return the sum of all numbers.
 */
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
public class Trebuchet1 {

    public static void main(String[] args) {
        // read in input file
        try (BufferedReader reader = new BufferedReader(new FileReader("./input.txt"))) {
            // parse each line char by char
            String line;
            Integer sum = 0;
            while ((line = reader.readLine()) != null) {
                char[] chars = {0,0};
                Boolean checkedFirst = false;
                // try to convert every char to an integer
                for (char ch : line.toCharArray()) {
                    if (!Character.isDigit(ch)) {
                        continue;
                    }
                    if (!checkedFirst) {
                        chars[0] = ch;
                        checkedFirst = true;
                    }
                    chars[1] = ch;
                }
                String digits = new String(chars);
                try {
                    sum += Integer.parseInt(digits);
                } catch (NumberFormatException e) {
                    System.out.println("Unable too convert digits to integer: " + digits);
                }
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