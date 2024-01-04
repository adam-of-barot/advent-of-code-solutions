# Lens Library Part #2

# Go through the initialization sequence,
# and fill al 256 boxes with the appropriate lenses.
# Calculate the total focusing power of the boxes.

def custom_hash(string: str):
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

    boxes = [{} for i in range(0,256)]

    for step in sequence:
        # determine label and operation to perform
        last_str = step[-1]
        # there are two possible scenarios
        # 1A) label is followed by "=" and a number from 1 to 9 -> add lens to box with focal power defined by number
        # 1B) if lens already in box, overwrite focal power
        # 2) label is followed by "-" -> remove lens with that label from box
        try:
            # try to add a lens to a box
            focal_length = int(last_str)
            label = step[:-2]
            idx = custom_hash(label)
            box = boxes[idx]
            box[label] = focal_length
        except ValueError:
            # remove a lens if cannot convert to int
            label = step[:-1]
            idx = custom_hash(label)
            box = boxes[idx]
            try:
                del box[label]
            except KeyError:
                pass
    
    # calculate total focusing power
    total_focusing_power = 0

    for box_idx, box in enumerate(boxes):
        slot = 1
        for focal_length in box.values():
            total_focusing_power += (1 + box_idx) * slot * focal_length
            slot += 1

    print(total_focusing_power)


if __name__ == "__main__":
    main("./input.txt")