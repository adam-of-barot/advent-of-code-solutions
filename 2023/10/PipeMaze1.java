import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * PipeMaze Part #1
 * 
 * Find the position in the maze farthest from the starting position.
 */
public class PipeMaze1 {

    public static ArrayList<ArrayList<Tile>> maze;
    public static int[] startPos = new int[2];

    public static void main(String[] args) {
        loadData("./input.txt");

        Tile startTile = maze.get(startPos[0]).get(startPos[1]);

        // we need to find the one giant loop
        // there are no junction tiles
        // one tile can only connect to 2 other tiles
        // if we are traversing a loop, that means there is only one correct "next" tile from the current one

        // loop through possible starting tiles of the loop
        // keep count of the tiles in the loop
        // if we encounter a dead end, jump to the next possible start tile

        for (Tile currentTile : getConnectingTiles(startTile)) {

            if (currentTile.traversed) {
                break;
            }

            ArrayList<Tile> currentRoute = new ArrayList<>();

            while (!currentTile.type.equals("S")) {
                System.out.println(currentTile.type);
                setTraversedTrue(currentTile);
                currentRoute.add(currentTile);
                try {
                    currentTile = getNextTile(currentTile);
                } catch (DeadEndException e) {
                    break;
                }
            }

            System.out.println(currentRoute.size());
            System.out.println(getFarthestPoint(currentRoute.size()));
        }

    }

    public static int getFarthestPoint(int routeSize) {
        return (routeSize + 1) / 2;
    }

    public static Tile getNextTile(Tile tile) throws DeadEndException {
        try {
            ArrayList<Tile> neighbours = getConnectingTiles(tile);
            return neighbours.stream().filter(t -> !t.traversed).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new DeadEndException();
        }
    }

    public static ArrayList<Tile> getConnectingTiles(Tile tile) {
        ArrayList<Tile> neighbours = new ArrayList<>();

        for (int row = tile.row - 1; row <= tile.row + 1; row++) {
            for (int col = tile.col - 1; col <= tile.col + 1; col++) {
                if ((col == tile.col) && (row == tile.row)) {
                    continue;
                }
                if ((col != tile.col) && (row != tile.row)) {
                    continue;
                }
                try {
                    Tile neighbour = getTileFromMaze(row, col);
                    boolean canConnect = tile.canConnect(neighbour);
                    if (canConnect) {
                        neighbours.add(neighbour);
                    }
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }

        return neighbours;
    }

    public static Tile getTileFromMaze(int row, int col) {
        return maze.get(row).get(col);
    }

    public static void setTraversedTrue(Tile tile) {
        getTileFromMaze(tile.row, tile.col).traversed = true;
    }

    public static void loadData(String fileName) {
        maze = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int rowIdx = 0;
            while ((line = reader.readLine()) != null) {
                char[] chars = line.toCharArray();
                ArrayList<Tile> row = new ArrayList<>();
                int colIdx = 0;
                for (char ch : chars) {
                    String str = String.valueOf(ch);
                    Tile tile = new Tile(str, rowIdx, colIdx);
                    if (str.equals("S")) {
                        startPos[0] = rowIdx;
                        startPos[1] = colIdx;
                        tile.traversed = true;
                    }
                    row.add(tile);
                    colIdx++;
                }
                maze.add(row);
                rowIdx++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}