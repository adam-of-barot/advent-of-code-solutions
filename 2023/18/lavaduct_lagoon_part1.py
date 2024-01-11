# Lavaduct Lagoon Part #1

import numpy as np

class Tile:
    dug_out: bool
    color: str

    def __init__(self) -> None:
        self.dug_out = False
    
    def __str__(self) -> str:
        return "#" if self.dug_out else "."
    
    def dig(self) -> None:
        self.dug_out = True
    
    def set_color(self, color: str) -> None:
        self.color = color


def write_grid(array: np.ndarray, filename: str) -> None:
    lines = []
    for row in array:
        s = ""
        for col in row:
            s += col.__str__()
        lines.append(f"{s}\n")

    with open(filename, "w") as file:
        file.writelines(lines)


def count_grid(array: np.ndarray) -> int:
    count = 0
    for row in array:
        for col in row:
            count += int(col.dug_out)
    return count


def main(filename: str):

    positions: list[tuple[int, int, str]] = []
    x_pos_list: list[int] = []
    y_pos_list: list[int] = []
    
    with open(filename, "r") as file:
        # starting positions
        x = 0
        y = 0

        positions.append((x, y, ""))

        while line := file.readline():
            dir, step, color = line.strip().split(" ", 3)
            step = int(step)
            color = color.replace("(", "").replace(")", "")
    
            # figure out the dimension of the grid by going through the instruction list
            if dir == "L":
                x -= step
            elif dir == "R":
                x += step
            elif dir == "U":
                y -= step
            elif dir == "D":
                y += step
            
            positions.append((x, y, color))
            x_pos_list.append(x)
            y_pos_list.append(y)
        
    max_x = max(x_pos_list)
    min_x = min(x_pos_list)
    max_y = max(y_pos_list)
    min_y = min(y_pos_list)

    # create grid
    grid = np.array([
        [Tile() for x in range(min_x, max_x+1)] for y in range(min_y, max_y+1)
    ], np.object_)

    # go through positions again, "dig out" the grid edge
    for i in range(len(positions)):
        try:
            current = positions[i]
            next = positions[i+1]
            color = next[2]

            x_vals = [current[0] - min_x, next[0] - min_x]
            y_vals = [current[1] - min_y, next[1] - min_y]
            x_vals.sort()
            y_vals.sort()

            if y_vals[0] == y_vals[1]:
                tiles = grid[y_vals[0], x_vals[0] or None : x_vals[1]+1 or None]
            else:
                tiles = grid[y_vals[0] or None : y_vals[1]+1 or None, x_vals[0]]

            for t in tiles:
                t.dig()
                t.set_color(color)

        except IndexError:
            break
    
    # go through grid, fill out parts between edges using the scanline method
    filling = False

    #write_grid(grid, "./unfilled.txt")
    
    for row_idx in range(1, len(grid)-1):
        row = grid[row_idx]
        on_edge = False
        above_edge_start: Tile | None = None
        above_edge_end: Tile | None = None
        for col in range(len(row)):
            try:
                current_tile = row[col]
                next_tile = row[col+1]
                if current_tile.dug_out:
                    # enterring a horizontal edge
                    # record tile above the start tile of the edge
                    if next_tile.dug_out:
                        if not on_edge:
                            on_edge = True
                            above_edge_start = grid[row_idx+1][col]
                    else:
                        # exiting a horizontal edge
                        # record tile above end of the edge
                        # if start and end tile's match -> U turn -> don't change filling
                        # don't match -> L turn -> change filling
                        if on_edge:
                            on_edge = False
                            above_edge_end = grid[row_idx+1][col]
                            if above_edge_start and above_edge_end and above_edge_start.dug_out != above_edge_end.dug_out:
                                filling = not filling
                        else:
                            filling = not filling
                    continue
                if filling:
                    current_tile.dig()
            except IndexError:
                filling = False
                break

    write_grid(grid, "./filled.txt")
        
    print(count_grid(grid))


if __name__ == "__main__":
    main("./input.txt")