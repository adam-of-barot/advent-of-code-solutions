import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * HauntedWasteland Part #1
 * 
 * Follow the left/right instructions, until you reach ZZZ node from AAA.
 */
public class HauntedWasteland1 {

    public static String instructions;
    public static HashMap<String, String[]> nodeMap;

    public static void main(String[] args) {
        loadData("./input.txt");
        String node = "AAA";
        String[] directions = nodeMap.get("AAA");
        int steps = 0;
        while (!node.equals("ZZZ")) {
            for (char ch : instructions.toCharArray()) {
                steps += 1;
                String inst = String.valueOf(ch);
                node = inst.equals("L") ? directions[0] : directions[1];
                if (node.equals("ZZZ")) {
                    break;
                }
                directions = nodeMap.get(node);
            }
        };
        System.out.println(steps);
    }

    public static void loadData(String fileName) {
        nodeMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            // get instructions
            instructions = reader.readLine();
            // read empty line
            reader.readLine();
            // get node list
            while ((line = reader.readLine()) != null) {
                String key = line.substring(0, 3);
                String left = line.substring(7, 10);
                String right = line.substring(12, 15);
                //System.out.println(String.format("%s %s %s", key, left, right));
                nodeMap.put(key, new String[]{left, right});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}