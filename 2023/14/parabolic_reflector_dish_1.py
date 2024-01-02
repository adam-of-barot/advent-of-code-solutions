# Parabolic Reflector Dish Part #1

# Goal:
# Shift all rounded rocks (O) in the input to north.
# Cube-shaped rocks (#) do not move, and will stop other rocks from moving further.
# Calculate the load on the northern beam.
# 1 rock's load = distance from southern end (end of file)
# Sum up all the loads of the rounded rocks.

import numpy as np

def main(filename):

    # load input into 2D string array

    with open(filename, "r") as file:
        arr: list[list[str]] = []
        while line := file.readline().strip():
            l = [s for s in line]
            arr.append(l)
    
    platform = np.array(arr, np.str_)
    total_load = 0

    # cycle through columns
    for col in range(0, platform.shape[1]):

        top = 0  # northernmost row index where rocks will stop sliding

        # cycle through rows
        for row in range(0, platform.shape[0]):
            element = platform[row, col]
            # ignore empty spaces
            if element == ".":
                continue
            # if we hit a cube rock, set top variable to that index
            elif element == "#":
                top = row + 1
            # if we hit a round rock, and there is space above it
            # swap it's position with top + 1 element
            elif element == "O":
                pos = row
                if pos > top:
                    pos = top
                # record rock's load
                load = platform.shape[0] - pos
                total_load += load
                # increment top position
                top += 1
    
    print(total_load)

if __name__ == "__main__":
    main("./input.txt")