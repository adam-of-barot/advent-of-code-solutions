# The Floor Will Be Lava Part #2

# Find the configuration that produces the most amount of energized tiles.
# Light beams can start from all edge tiles.

# Map detailing how mirrors and splitters deflect a beam
# Keys = (tile symbol, travel direction)
# Values = (travel direction, travel direction)
direction_map: dict[tuple[str, str], tuple[str, str | None]] = {
    # Mirrors
    # Values are tuples with only one valid travel direction
    ("/", "R"): ("U", None),
    ("/", "D"): ("L", None),
    ("/", "U"): ("R", None),
    ("/", "L"): ("D", None),
    ("\\", "L"): ("U", None),
    ("\\", "D"): ("R", None),
    ("\\", "U"): ("L", None),
    ("\\", "R"): ("D", None),

    # Splitters
    # Values with two travel directions should spawn a new beam
    ("|", "L"): ("U", "D"),
    ("|", "R"): ("U", "D"),
    ("-", "U"): ("L", "R"),
    ("-", "D"): ("L", "R"),
    # Values with one travel directions let the beam pass through unchanged
    ("|", "U"): ("U", None),
    ("|", "D"): ("D", None),
    ("-", "L"): ("L", None),
    ("-", "R"): ("R", None),
}

class Tile:
    symbol: str
    is_energized: bool
    directions: list[str]
    row: int
    col: int

    def __init__(self, symbol: str, pos: tuple[int, int]):
        self.symbol = symbol
        self.row = pos[0]
        self.col = pos[1]
        self.directions = []
        self.is_energized = False
    
    def __str__(self):
        return f"{self.symbol} ({self.row},{self.col})"
    
    def energize(self):
        self.is_energized = True
    
    def reset(self):
        self.is_energized = False
        self.directions.clear()
    
    def add_direction(self, direction: str):
        self.directions.append(direction)


class Beam:
    id: int
    direction: str
    row: int
    col: int

    def __init__(self, id: int, direction: str, pos: tuple[int, int]):
        self.id = id
        self.direction = direction
        self.row = pos[0]
        self.col = pos[1]
    
    def __str__(self):
        return f"Beam #{self.id} @ ({self.row},{self.col})"
    
    def get_new_direction(self, tile: Tile):
        if tile.symbol == ".":
            return (self.direction, None)

        key = (tile.symbol, self.direction)
        new_directions = direction_map[key]

        return new_directions
    
    def set_direction(self, direction: str):
        self.direction = direction
    
    def travel(self):
        if self.direction == "R":
            self.col += 1
        elif self.direction == "L":
            self.col -= 1
        elif self.direction == "D":
            self.row += 1
        elif self.direction == "U":
            self.row -= 1


def main(filename: str):

    # create 2D string array

    grid: list[list[Tile]] = []
    counts = []

    with open(filename, "r") as file:
        row = 0
        while line := file.readline():
            grid.append([Tile(s, (row, col)) for col, s in enumerate(line.strip())])
            row += 1
    
    # cycle through edge tiles
    for row in grid:
        for start_tile in row:

            # detect edge tile, and determine start direction
            start_directions: list[str] = []
            if start_tile.row == 0:
                start_directions.append("D")
            if start_tile.col == 0:
                start_directions.append("R")
            if start_tile.row == len(grid) - 1:
                start_directions.append("U")
            if start_tile.col == len(grid[0]) - 1: 
                start_directions.append("L")
            if len(start_directions) == 0:
                continue

            for dir in start_directions:

                # store spawned beams in a list
                # create a starting beam
                beam_id = 1
                start_beam = Beam(beam_id, dir, (start_tile.row, start_tile.col))
                beams: list[Beam] = [start_beam]
                beam_id += 1

                while len(beams) > 0:
                    # cycle through beams
                    for beam in beams:

                        try:

                            # remove beams that are out-of-bound
                            if beam.row < 0 or beam.col < 0:
                                raise IndexError

                            tile = grid[beam.row][beam.col]

                            # remove beam if it has already traveled through this tile in the same direction
                            if beam.direction in tile.directions:
                                beams.remove(beam)
                                continue

                            # energize the tile the beam is currently on
                            tile.energize()
                            # add travel direction to tile's list
                            tile.add_direction(beam.direction)

                            # figure out if beam should travel in another direction
                            new_directions = beam.get_new_direction(tile)
                            beam.set_direction(new_directions[0])

                            if new_directions[1] is not None:
                                new_beam = Beam(beam_id, new_directions[1], (beam.row, beam.col))
                                beam_id += 1
                                new_beam.travel()
                                beams.append(new_beam)

                            # move beam by 1 tile to the direction it should travel
                            beam.travel()

                        except IndexError:
                            beams.remove(beam)


                energized_count = 0
                for row in grid:
                    for t in row:
                        if t.is_energized:
                            energized_count += 1
                        t.reset()
                counts.append(energized_count)
    
    print(max(counts))

if __name__ == "__main__":
    main("./input.txt")