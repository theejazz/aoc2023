package day12;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Part24 {

    public static void main(String[] args) throws IOException {
        List<String> records = new ArrayList<>();
        try (InputStream input = Part24.class.getClassLoader().getResourceAsStream("day12.input");
             Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine());
            }
        }

        List<String> expanded = records.stream().map(rec -> {
            String[] split = rec.split(" ");
            String expStr = String.join("?", Collections.nCopies(5, split[0]));
            String expNum = String.join(",", Collections.nCopies(5, split[1]));
            return expStr + " " + expNum;
        }).toList();

        List<List<String>> possibilities = new ArrayList<>();
        for (int idx = 0; idx != expanded.size(); ++idx) {
            possibilities.add(ConditionRecords.determineSolutions(expanded.get(idx)));
            System.out.println(idx);
        }
        long sum = possibilities.stream().mapToInt(List::size).sum();
        System.out.println(sum);
    }

}