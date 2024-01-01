# Point of Incidence Part #1

# Goal: find the line of reflection in the input patterns
# Solution: number of columns to the left of vertical lines + 100 * number of rows above horzontal lines

import numpy as np

def mirror_check(original: np.ndarray) -> int:

    # move hypothical mirror line down
    # create two sub-lists ranging from either end
    # sort them
    # flip the bottom sublist
    # test for equality

    pattern = np.copy(original)

    l = len(pattern)  # len is always odd

    for idx in range(0, l - 1):
        # idx: the element directly above the mirror line
        # figure out which is closer: the start or the end of the pattern
        start_dist = idx+1
        end_dist = l - idx - 1
        width = min(start_dist, end_dist)

        if start_dist < end_dist:
            top_start = 0
            top_end = idx+1
            bot_start = top_end
            bot_end = bot_start+width                
        else:
            top_start = idx - width + 1
            top_end = idx+1
            bot_start = top_end
            bot_end = None
        
        top_sublist = pattern[top_start:top_end]
        bottom_sublist = pattern[bot_start:bot_end]

        bottom_sublist = np.flip(bottom_sublist, axis=0)

        #print(idx, top_sublist, bottom_sublist)

        if np.array_equal(top_sublist, bottom_sublist):
            return idx+1
    
    return 0

def main(filename: str):

    patterns: list[np.ndarray] = []
    temp_array: list[list[str]] = []

    with open(filename, "r") as file:

        while line := file.readline():
            line = line.strip()
            if line:
                # construct string array
                data = [s for s in line]
                temp_array.append(data)
            else:
                # jump to next row
                patterns.append(np.array(temp_array, np.str_))
                temp_array.clear()
    
    horizontalValues = 0
    verticalValues = 0

    for pattern in patterns:

        # test for horizontal mirror line

        print(pattern)

        horizontalValue = mirror_check(pattern)
        horizontalValues += horizontalValue
        print(horizontalValue)

        if not horizontalValue:

            # test for vertical mirror line
            # do the same as above, but with a transposed pattern array

            print("----")
            transposed = pattern.transpose()
            print(transposed)
            verticalValue = mirror_check(transposed)
            verticalValues += verticalValue
            print(verticalValue)
        
    print(verticalValues + 100 * horizontalValues)

if __name__ == "__main__":
    main("./input.txt")