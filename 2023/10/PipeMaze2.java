import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * PipeMaze Part #1
 * 
 * Find the number of tiles enclosed within the loop.
 */
public class PipeMaze2 {

    public static ArrayList<ArrayList<Tile>> maze;
    public static int[] startPos = new int[2];

    public static void main(String[] args) {
        loadData("./input.txt");

        Tile startTile = maze.get(startPos[0]).get(startPos[1]);
        ArrayList<Tile> currentRoute = new ArrayList<>();

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

            currentRoute.clear();
            currentRoute.add(startTile);

            while (!currentTile.type.equals("S")) {
                setTraversedTrue(currentTile);
                currentRoute.add(currentTile);
                try {
                    currentTile = getNextTile(currentTile);
                } catch (DeadEndException e) {
                    break;
                }
            }

            if (currentTile.type.equals("S")) {
                break;
            }

        }

        // loop through the maze again row-by-row
        // if you encounter a tile that is part of the loop*, flip a variable
        // while that variable is true, count all non-loop tiles in the loop
        // apparently, this is called a "scanline" approach

        // *Note: it's not that straightforward
        // vertical pipes always flip the variable
        // horizontal pipes don't
        // bends flip it depending on how they are paired
        // F + J -> S shape, flip
        // F + 7 -> U shape, don't flip
        // L + J -> U shape, don't flip
        // L + 7 -> S shape, flip

        int insideLoopCount = 0;

        for (ArrayList<Tile> row : maze) {
            boolean shouldCount = false;
            Tile cornerStart = null;
            for (Tile tile : row) {
                if (currentRoute.contains(tile)) {
                    switch (tile.type) {
                        case "|":
                            shouldCount = !shouldCount;
                            break;
                        case "F":
                        case "L":
                            cornerStart = tile;
                            break;
                        case "J":
                            if (cornerStart != null && cornerStart.type.equals("F")) {
                                shouldCount = !shouldCount;
                            }
                            cornerStart = null;
                            break;
                        case "7":
                            if (cornerStart != null && cornerStart.type.equals("L")) {
                                shouldCount = !shouldCount;
                            }
                            cornerStart = null;
                            break;
                        default:
                            break;
                    }
                } else if (shouldCount) {
                    insideLoopCount++;
                }
            }
        }

        System.out.println(insideLoopCount);

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