import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * MirageMaintenance Part #2
 * 
 * Take each line, and calculate the difference between it's elements.
 * This gives you a new array.
 * If this array is not all zeroes, create a new array of differences.
 * Do this, until the new array is full of zeroes.
 * Then, calculate the first element in the original array.
 * Sum up all the predictions.
 */
public class MirageMaintenance2 {

    public static ArrayList<ArrayList<Long>> histories;

    public static void main(String[] args) {
        loadData("./input.txt");

        Long sum = 0L;

        for (ArrayList<Long> original : histories) {
            // this is goint ot hold all the difference arrays
            ArrayList<ArrayList<Long>> diffArray = new ArrayList<>();
            diffArray.add(original);
            // set the current array to the original array
            ArrayList<Long> currentArray = original;
            // if the current array is not all zeroes,
            // then cycle through the current array, and generate the next one
            while (!isAllZero(currentArray)) {
                ArrayList<Long> nextArray = new ArrayList<>();
                for (int i = 0; i < currentArray.size(); i++) {
                    try {
                        Long diff = currentArray.get(i+1) - currentArray.get(i);
                        nextArray.add(diff);
                    } catch (IndexOutOfBoundsException e) {
                        // add new array to the beginning
                        // this way, you don't have to reverse it later
                        diffArray.add(0, nextArray);
                        currentArray = nextArray;
                        break;
                    }
                }
            }

            // create prediction by cycling through the diff array

            for (int i = 1; i < diffArray.size(); i++) {
                ArrayList<Long> thisArray = diffArray.get(i);
                ArrayList<Long> prevArray = diffArray.get(i - 1);
                Long thisValue = firstValue(thisArray);
                Long prevValue = firstValue(prevArray);
                Long diff = thisValue - prevValue;
                thisArray.add(0, diff);
            }

            Long prediction = firstValue(diffArray.get(diffArray.size() - 1));

            sum += prediction;
        }

        System.out.println(sum);
    }

    public static void loadData(String fileName) {
        histories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strArray = line.split(" ");
                ArrayList<Long> numArray = new ArrayList<>();
                for (String str : strArray) {
                    numArray.add(Long.parseLong(str));
                }
                histories.add(numArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAllZero(List<Long> array) {
        for (Long element : array) {
            if (!element.equals(0L)) {
                return false;
            }
        }
        return true;
    }

    public static Long lastValue(List<Long> array) {
        return array.get(array.size() - 1);
    }

    public static Long firstValue(List<Long> array) {
        return array.get(0);
    }
}