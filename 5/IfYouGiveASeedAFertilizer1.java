import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * IfYouGiveASeedAFertilizer Part #1
 * 
 * Find the lowest location number that corresponds to any of the initials seeds.
 */
public class IfYouGiveASeedAFertilizer1 {

    public static void main(String[] args) {
        ArrayList<Long> seeds = loadSeeds("./data/seeds.txt");
        ArrayList<Long[]> soils = loadMap("./data/seed-to-soil.txt");
        ArrayList<Long[]> fertilizers = loadMap("./data/soil-to-fertilizer.txt");
        ArrayList<Long[]> waters = loadMap("./data/fertilizer-to-water.txt");
        ArrayList<Long[]> lights = loadMap("./data/water-to-light.txt");
        ArrayList<Long[]> temps = loadMap("./data/light-to-temp.txt");
        ArrayList<Long[]> humidities = loadMap("./data/temp-to-humidity.txt");
        ArrayList<Long[]> locations = loadMap("./data/humidity-to-location.txt");
        ArrayList<Long> finalLocations = new ArrayList<Long>();
        for (Long seed : seeds) {
            Long soil = convert(seed, soils);
            Long fertilizer = convert(soil, fertilizers);
            Long water = convert(fertilizer, waters);
            Long light = convert(water, lights);
            Long temp = convert(light, temps);
            Long humidity = convert(temp, humidities);
            Long location = convert(humidity, locations);
            finalLocations.add(location);
        }
        System.out.println(Collections.min(finalLocations));
    }

    public static ArrayList<Long> loadSeeds(String fileName) {
        ArrayList<Long> seeds = new ArrayList<Long>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] strings = line.split(" ");
                for (String str : strings) {
                    seeds.add(Long.parseLong(str));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seeds;
    }

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
        return map;
    }

    public static Long convert(Long source, ArrayList<Long[]> map) {
        for (Long[] m : map) {
            Long destStart = m[0];
            Long sourceStart = m[1];
            Long range = m[2];
            // if source number is in a defined range, return corresponding mapped value
            if (sourceStart <= source && source <= sourceStart + range) {
                Long offset = sourceStart - destStart;
                return source - offset;
            }
        }
        return source;
    }
}