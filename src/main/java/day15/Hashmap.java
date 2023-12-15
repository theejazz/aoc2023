package day15;

import java.util.*;

class Lens {
    String label;
    int focalLength;

    Lens(String label, int focalLength) {
        this.label = label;
        this.focalLength = focalLength;
    }
}

class Hashmap {

    Map<Integer, List<Lens>> boxes;

    Hashmap() {
        boxes = new HashMap<>();
        for (int idx = 0; idx != 256; ++idx) {
            boxes.put(idx, new ArrayList<>());
        }
    }

    static int hash(String str) {
        return str.chars().reduce(0, Hashmap::hash);
    }

    static int hash(int left, int right) {
        left += right;
        left *= 17;
        left %= 256;
        return left;
    }

    void parse(String str) {
        if (str.endsWith("-")) {
            remove(str);
        } else {
            add(str);
        }
    }

    void remove(String str) {
        String label = str.substring(0, str.length() - 1);
        int hash = hash(label);
        boxes.get(hash).removeIf(l -> label.equalsIgnoreCase(l.label));
    }

    void add(String str) {
        String[] split = str.split("=");
        String label = split[0];
        int focalLength = Integer.parseInt(split[1]);
        int hash = hash(label);
        Optional<Lens> lens = boxes.get(hash).stream().filter(l -> label.equalsIgnoreCase(l.label)).findFirst();
        if (lens.isPresent()) {
            lens.get().focalLength = focalLength;
        } else {
            boxes.get(hash).add(new Lens(label, focalLength));
        }
    }

    int calcFocusingPower() {
        int power = 0;
        for (Map.Entry<Integer, List<Lens>> box : this.boxes.entrySet()) {
            int boxPower = 0;
            List<Lens> lenses = box.getValue();
            for (int idx = 0; idx != lenses.size(); ++idx) {
                boxPower += (idx + 1) * lenses.get(idx).focalLength;
            }

            power += boxPower * (box.getKey() + 1);
        }

        return power;
    }

}
