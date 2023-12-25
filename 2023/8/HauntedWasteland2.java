import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * HauntedWasteland Part #2
 * 
 * Follow the left/right instructions, until you reach all nodes ending in Z from all nodes ending in A.
 */
public class HauntedWasteland2 {

    public static String instructions;
    public static ArrayList<String> nodes;
    public static HashMap<String, String[]> nodeMap;

    public static void main(String[] args) {
        loadData("./input.txt");

        // so apparently, this thing can be solved with the Least Common multiple (LCM) method
        // each starting node has only one end node
        // find how many steps each starting node takes to reach it's end
        // then find the lcm of these steps

        ArrayList<Long> allSteps = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i++) {
            long steps = 0;
            String node = nodes.get(i);
            while (!isEndNode(node)) {
                for (char ch : instructions.toCharArray()) {
                    steps += 1;
                    String[] directions = nodeMap.get(node);
                    String inst = String.valueOf(ch);
                    node = inst.equals("L") ? directions[0] : directions[1];
                    if (isEndNode(node)) {
                        break;
                    }
                }
            }
            allSteps.add(steps);
        }

        for (long step : allSteps) {
            System.out.println(step);
        }
        System.out.println(lcm(allSteps.toArray(Long[]::new)));
    }

    public static Long gcd(Long a, Long b) {
        while (b > 0) {
            Long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static Long gcd (Long[] input) {
        Long output = input[0];
        for (int i = 1; i < input.length; i++) {
            output = gcd(output, input[i]);
        }
        return output;
    }

    public static Long lcm(Long a, Long b) {
        return a * (b / gcd(a, b));
    }

    public static Long lcm(Long[] input) {
        Long output = input[0];
        for (int i = 1; i < input.length; i++) {
            output = lcm(output, input[i]);
        }
        return output;
    }

    public static boolean isEndNode(String node) {
        return node.substring(2).equals("Z");
    }

    public static boolean isStartNode(String node) {
        return node.substring(2).equals("A");
    }

    public static void loadData(String fileName) {
        nodeMap = new HashMap<>();
        nodes = new ArrayList<>();
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
                // figure out starting nodes
                if (isStartNode(key)) {
                    nodes.add(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}