package day12;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Part23 {

    public static void main(String[] args) throws IOException {
        List<String> records = new ArrayList<>();
        try (InputStream input = Part23.class.getClassLoader().getResourceAsStream("day12.input");
             Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine());
            }
        }

        List<List<String>> possibilities = records.stream().map(ConditionRecords::determineSolutions).toList();
        long sum = possibilities.stream().mapToInt(List::size).sum();
        System.out.println(sum);
    }

}
