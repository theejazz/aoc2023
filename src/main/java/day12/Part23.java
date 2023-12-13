package day12;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Part23 {

    public static void main(String[] args) throws IOException {
        List<String> records = new ArrayList<>();
        try (InputStream input = new FileInputStream("src/main/resources/day12.input");
             Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine());
            }
        }

        List<Long> possibilities = records.stream().map(ConditionRecord::fromString).map(ConditionRecord::findNumberOfSolutions).toList();
        long sum = possibilities.stream().mapToLong(i -> (long) i).sum();
        System.out.println(sum);
    }

}
