/**
 * CosmicExpansion1
 */
public class CosmicExpansion1 {

    public static void main(String[] args) {
        Universe universe = new Universe("./input.txt", 1000000);
        long sum = 0;
        for (int i = 0; i < universe.galaxies.size(); i++) {
            for (int j = i + 1; j < universe.galaxies.size(); j++) {
                Galaxy g1 = universe.galaxies.get(i);
                Galaxy g2 = universe.galaxies.get(j);
                int shortestPath = g1.findShortestPath(g2);
                //System.out.println(String.format("%s %s %s", g1, g2, shortestPath));
                sum += shortestPath;
            }
        }
        System.out.println(sum);
    }

}