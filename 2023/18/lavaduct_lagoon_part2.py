# Lavaduct Lagoon Part #2

def main(filename: str):

    positions: list[tuple[int, int]] = []
    dir_map = {"0": "R", "1": "D", "2": "L", "3": "U"}
    outer = 0
    
    with open(filename, "r") as file:
        # starting positions
        x = 0
        y = 0

        positions.append((x, y))

        while line := file.readline():
            _, _, color = line.strip().split(" ", 3)
            color = color.replace("(", "").replace(")", "").replace("#", "")
            step = int(color[0:5], 16)
            dir = dir_map.get(color[5])
    
            # figure out the dimension of the grid by going through the instruction list
            if dir == "L":
                x -= step
            elif dir == "R":
                x += step
            elif dir == "U":
                y -= step
            elif dir == "D":
                y += step
            
            positions.append((x, y))
            outer += step
        
    # the method that I used in part 2 will never finish
    # try something else
    # There is something called the Shoelace formula, and Pick's theorem

    # Shoelace part
    s1 = 0
    s2 = 0
    for i in range(len(positions)):
        try:
            s1 += positions[i][0] * positions[i+1][1]
            s2 += positions[i][1] * positions[i+1][0]
        except IndexError:
            s1 += positions[-1][0] * positions[0][1]
            s2 += positions[-1][1] * positions[0][0]
    
    inner = 0.5 * abs(s1 - s2)
    
    # Pick's theorem part
    area = inner + outer/2 + 1

    print(area)


if __name__ == "__main__":
    main("./input.txt")