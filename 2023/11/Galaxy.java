public class Galaxy {
    public int id;
    public int row;
    public int col;

    public Galaxy(int id, int row, int col) {
        this.id = id;
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return String.format("Galaxy #%s (%s,%s)", this.id, this.row, this.col);
    }

    public int findShortestPath(Galaxy otherGalaxy) {
        //System.out.println(this);
        //System.out.println(otherGalaxy);
        int rowDiff = Math.abs(this.row - otherGalaxy.row);
        int colDiff = Math.abs(this.col - otherGalaxy.col);
        int output = rowDiff + colDiff;
        //System.out.println(output);
        //System.out.println("----");
        return output;
    }
}
