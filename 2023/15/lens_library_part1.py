# Lens Library Part #1

# Calculate the hash of each comma-seperated string
# Sum up the hash results

def hash(string: str):
    val = 0
    for s in string:
        val += ord(s)
        val *= 17
        val %= 256
    return val

def main(filename: str):
    with open(filename, "r") as file:
        line = file.readline()
        sequence = line.split(",")

    hash_sum = 0

    for step in sequence:
        hash_sum += hash(step)

    print(hash_sum)

if __name__ == "__main__":
    main("./input.txt")