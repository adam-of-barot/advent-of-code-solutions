# Parabolic Reflector Dish Part #2

# Goal:
# Shift all rounded rocks (O) in the input to north, then west, then south, then east.
# Do this 1000000000 times.
# Cube-shaped rocks (#) do not move, and will stop other rocks from moving further.
# Calculate the load on the northern beam.
# 1 rock's load = distance from southern end (end of file)
# Sum up all the loads of the rounded rocks.

import numpy as np
import ast

def slide_north(platform: np.ndarray):
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
                if row > top:
                    # perform swap
                    platform[row, col] = "."
                    platform[top, col] = "O"
                # increment top position
                top += 1

def slide_west(platform: np.ndarray):
    for row in range(0, platform.shape[0]):

        left = 0

        for col in range(0, platform.shape[1]):
            element = platform[row, col]
            if element == ".":
                continue
            elif element == "#":
                left = col + 1
            elif element == "O":
                if left < col:
                    platform[row, col] = "."
                    platform[row, left] = "O"
                left += 1

def slide_south(platform: np.ndarray):
    for col in range(platform.shape[1]-1, -1, -1):

        bot = platform.shape[0]-1

        for row in range(platform.shape[0]-1, -1, -1):
            element = platform[row, col]
            if element == ".":
                continue
            elif element == "#":
                bot = row - 1
            elif element == "O":
                if row < bot:
                    platform[row, col] = "."
                    platform[bot, col] = "O"
                bot -= 1

def slide_east(platform: np.ndarray):
    for row in range(platform.shape[0]-1, -1, -1):

        right = platform.shape[1]-1

        for col in range(platform.shape[1]-1, -1, -1):
            element = platform[row, col]
            if element == ".":
                continue
            elif element == "#":
                right = col - 1
            elif element == "O":
                if col < right:
                    platform[row, col] = "."
                    platform[row, right] = "O"
                right -= 1

def cycle(platform: np.ndarray):
    slide_north(platform)
    slide_west(platform)
    slide_south(platform)
    slide_east(platform)

def calc_load(platform: list[list[str]]):

    total_load = 0
    rows = len(platform)

    for idx, row in enumerate(platform):
        for element in row:
            if element == "O":
                total_load += rows - idx
    
    return total_load

def main(filename: str, cycles: int):

    # load input into 2D string array

    with open(filename, "r") as file:
        arr: list[list[str]] = []
        while line := file.readline().strip():
            l = [s for s in line]
            arr.append(l)
    
    platform = np.array(arr, np.str_)

    states: dict[str, int] = {}
    state = ""

    for c in range(0, cycles):
        cycle(platform)
        state = platform.tolist().__str__()
        # try to access state
        try:
            states[state]
            break
        # save state
        except KeyError:
            states[state] = c
    
    # figure out which state to load back
    cycle_start = states[state]
    cycle_length = c - cycle_start
    offset = (cycles - cycle_start) % cycle_length - 1
    final_state_idx = cycle_start + offset
    print(f"Current cycle: {c}")
    print(f"Cycle start: {cycle_start}")
    print(f"Cycle length: {cycle_length}")
    print(f"Offset: {offset}")
    print(f"Final state idx: {final_state_idx}")
    final_state = ""
    for key, value in states.items():
        if value == final_state_idx:
            final_state = key
            break

    final_array = ast.literal_eval(final_state)
    print(calc_load(final_array))

if __name__ == "__main__":
    main("./input.txt", 1000000000)