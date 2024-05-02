# Step Counter Part 1

class Patch:
    row: int
    col: int
    active: bool
    discovered: bool

    def __init__(self, row: int, col: int) -> None:
        self.row = row
        self.col = col
        self.active = False
        self.explored = False
    
    def flip(self):
        self.active = not self.active
    
    def get_neighbour(self, grid: list[str], row: int, col: int, neighbours: list):
        try:
            string = grid[row][col]
            if "." == string:
                neighbours.append(Patch(row, col))
        except IndexError:
            return None
    
    def get_neighbours(self, grid: list[str]):
        neighbours: list[Patch] = []
        self.get_neighbour(grid, self.row - 1, self.col, neighbours)  # top
        self.get_neighbour(grid, self.row, self.col - 1, neighbours)  # left
        self.get_neighbour(grid, self.row, self.col + 1, neighbours)  # right
        self.get_neighbour(grid, self.row + 1, self.col, neighbours)  # bottom
        self.explored = True
        return neighbours


def main(filename: str, steps: int):

    grid: str = []
    patches: dict[tuple[int, int], Patch] = {}
    
    # parse input
    with open(filename, "r") as file:
        row = 0
        while line := file.readline().strip():
            grid.append(line)
            if "S" in line:
                col = line.index("S")
                start = Patch(row, col)
                start.flip()  # this should start out as active
                patches[(row, col)] = start
            row += 1

    for s in range(0, steps):
        newly_discovered: list[Patch] = []
        keys = patches.keys()
        for patch in patches.values():
            # flip active state
            patch.flip()
            # get neighbours
            if patch.explored:
                continue
            else:
                neighbours = patch.get_neighbours(grid)
                # if this is a new patch, add it
                for n in neighbours:
                    if (n.row, n.col) not in keys:
                        newly_discovered.append(n)

        # add new discoveries outside of loop
        for nd in newly_discovered:
            nd.flip()
            patches[(nd.row, nd.col)] = nd
    
    # count active patches
    active = 0
    for p in patches.values():
        if p.active:
            active += 1
    
    print(active)



if __name__ == "__main__":
    main("./input.txt", 64)