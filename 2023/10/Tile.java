import java.util.ArrayList;

public class Tile {
    public String type;
    public int row;
    public int col;
    public ArrayList<String> allowedConnections;
    public boolean ignore = false;
    public boolean traversed = false;
    
    public Tile (String type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        setConnections();
        if (type.equals(".")) {
            this.ignore = true;
        }
    }

    @Override
    public String toString() {
        return String.format("%s, row: %s, col: %s", this.type, this.row, this.col);
    }

    private void setConnections() {

        allowedConnections = new ArrayList<>();

        switch (this.type) {
            case "|":
                allowedConnections.add("N");
                allowedConnections.add("S");
                break;
            case "-":
                allowedConnections.add("E");
                allowedConnections.add("W");
                break;
            case "L":
                allowedConnections.add("N");
                allowedConnections.add("E");
                break;
            case "J":
                allowedConnections.add("N");
                allowedConnections.add("W");
                break;
            case "7":
                allowedConnections.add("S");
                allowedConnections.add("W");
                break;
            case "F":
                allowedConnections.add("S");
                allowedConnections.add("E");
                break;
            case "S":
                allowedConnections.add("N");
                allowedConnections.add("E");
                allowedConnections.add("S");
                allowedConnections.add("W");
                break;
            default:
                break;
        }
    }

    /**
     * Figure out from where this tile tries to connect.
     * 
     * @param neighbour
     * @return
     */
    private String connectFrom(Tile neighbour) {
        // possible connections: east & west
        if (neighbour.row == this.row) {
            if (neighbour.col > this.col) {
                return "W";
            } else {
                return "E";
            }
        // possible connections: north & south
        } else {
            if (neighbour.row > this.row) {
                return "N";
            } else {
                return "S";
            }
        }
    }
    
    /**
     * Figure out to where this tile tries to connect.
     * 
     * @param neighbour
     * @return
     */
    private String connectTo(Tile neighbour) {
        // possible connections: east & west
        if (neighbour.row == this.row) {
            if (neighbour.col > this.col) {
                return "E";
            } else {
                return "W";
            }
        // possible connections: north & south
        } else {
            if (neighbour.row > this.row) {
                return "S";
            } else {
                return "N";
            }
        }
    }

    public boolean canConnect(Tile tile) {
        // skip ground tiles
        if (tile.ignore) {
            return false;
        }
        // tiles do not connect diagonally
        if ((this.row != tile.row) && (this.col != tile.col)) {
            return false;
        }
        // tiles do not connect to themselves
        if ((this.row == tile.row) && (this.col == tile.col)) {
            return false;
        }
        String connectingFrom = connectFrom(tile);
        String connectingTo = connectTo(tile);
        boolean connectionOkay = tile.allowedConnections.contains(connectingFrom) && this.allowedConnections.contains(connectingTo);
        return connectionOkay;
    }
}
