import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * IfYouGiveASeedAFertilizer Part #2
 * 
 * Find the lowest location number that corresponds to any of the initials seeds.
 */
public class IfYouGiveASeedAFertilizer2 {

    public static Comparator<Long[]> sortByFirst = new Comparator<Long[]>() {
            @Override
            public int compare(Long[] lhs, Long[] rhs) {
                return Long.compare(lhs[0], rhs[0]);
            }
        };
    
    public static Comparator<Long[]> sortBySecond = new Comparator<Long[]>() {
            @Override
            public int compare(Long[] lhs, Long[] rhs) {
                return Long.compare(lhs[1], rhs[1]);
            }
        };

    public static void main(String[] args) {
        ArrayList<Long[]> seedRanges = loadSeeds("./data/seeds.txt");
        ArrayList<Long[]> seed2soil = loadMap("./data/seed-to-soil.txt");
        ArrayList<Long[]> soil2fert = loadMap("./data/soil-to-fertilizer.txt");
        ArrayList<Long[]> fert2water = loadMap("./data/fertilizer-to-water.txt");
        ArrayList<Long[]> water2light = loadMap("./data/water-to-light.txt");
        ArrayList<Long[]> light2temp = loadMap("./data/light-to-temp.txt");
        ArrayList<Long[]> temp2hum = loadMap("./data/temp-to-humidity.txt");
        ArrayList<Long[]> hum2loc = loadMap("./data/humidity-to-location.txt");


        printArrayList(seedRanges, "Seeds");
        
        ArrayList<Long[]> soilRanges = splitRange(seedRanges, seed2soil);

        printArrayList(soilRanges, "Soils");

        ArrayList<Long[]> fertRanges = splitRange(soilRanges, soil2fert);

        printArrayList(fertRanges, "Fertilizers");

        ArrayList<Long[]> waterRanges = splitRange(fertRanges, fert2water);
        
        printArrayList(waterRanges, "Waters");

        ArrayList<Long[]> lightRanges = splitRange(waterRanges, water2light);

        printArrayList(lightRanges, "Lights");

        ArrayList<Long[]> tempRanges = splitRange(lightRanges, light2temp);

        printArrayList(tempRanges, "Temps");

        ArrayList<Long[]> humRanges = splitRange(tempRanges, temp2hum);

        printArrayList(humRanges, "Humidities");

        ArrayList<Long[]> locRanges = splitRange(humRanges, hum2loc);

        printArrayList(locRanges, "Locations");
    }

    public static void printArray(Long[] element) {
        System.out.println(element[0] + "," + element[1]);
    }

    public static void printArrayList(ArrayList<Long[]> list, String listName) {
        System.out.println(listName);
        for (Long[] element : list) {
            printArray(element);
        };
        System.out.println("----");
    }

    /**
     * Load the seeds file
     */
    public static ArrayList<Long[]> loadSeeds(String fileName) {
        ArrayList<Long[]> seeds = new ArrayList<Long[]>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] strings = line.split(" ");
                for (int i = 0; i < strings.length;) {
                    Long a = Long.parseLong(strings[i]);
                    Long b = Long.parseLong(strings[i+1]);
                    seeds.add(new Long[]{a, b});
                    i += 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seeds;
    }

    /**
     * Load a map file
     */
    public static ArrayList<Long[]> loadMap(String fileName) {
        ArrayList<Long[]> map = new ArrayList<Long[]>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] strings = line.split(" ");
                Long[] temp = new Long[3];
                for (int i = 0; i < strings.length; i++) {
                    temp[i] = Long.parseLong(strings[i]);
                }
                map.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return a sorted list
        Collections.sort(map, sortBySecond);
        return map;
    }

    public static ArrayList<Long[]> splitRange(ArrayList<Long[]> ranges, ArrayList<Long[]> map) {
        ArrayList<Long[]> output = new ArrayList<Long[]>();
        while (ranges.size() > 0) {
            // always process the first item to not mess with indexes
            // look through map
            Boolean noMatch = true;
            Long[] range = ranges.get(0);
            Long rangeStart = range[0];
            Long rangeLen = range[1];
            Long rangeEnd = rangeStart + rangeLen - 1;
            for (Long[] mapEntry : map) {
                Long sourceStart = mapEntry[1];
                Long sourceEnd = mapEntry[1] + mapEntry[2] - 1;
                Long offset = rangeStart - sourceStart;
                Boolean startIsInRange = sourceStart <= rangeStart && rangeStart <= sourceEnd;
                Boolean endIsInRange = sourceStart <= rangeEnd && rangeEnd <= sourceEnd;
                Boolean isOverlapping = startIsInRange || endIsInRange;
                //System.out.println(rangeStart + "," + rangeEnd);
                //System.out.println(sourceStart + "," + sourceEnd);
                if (isOverlapping) {
                    noMatch = false;
                    // worst case: 1 range is getting split into 3 ranges
                    // because start and end is outside the source mapping range
                    // so there is going to be a new before, in and after range
                    if (!startIsInRange) {
                        Long[] beforeRange = new Long[]{rangeStart - 1, sourceStart - rangeStart};
                        ranges.add(beforeRange);
                        rangeStart = sourceStart;
                    }
                    if (!endIsInRange) {
                        Long[] afterRange = new Long[]{sourceEnd + 1, rangeEnd - sourceEnd};
                        ranges.add(afterRange);
                        rangeEnd = sourceEnd;
                    }
                    Long[] offsetted = new Long[]{mapEntry[0] + offset, rangeEnd - rangeStart + 1};
                    output.add(offsetted);
                    break;
                }
            }
            if (noMatch) {
                output.add(range);
            }
            ranges.remove(0);
        }
        Collections.sort(output, sortByFirst);
        return output;
    }
}