package day15;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Part29 {

    public static void main(String[] args) throws IOException {
        String strs;
        try (InputStream input = new FileInputStream("src/main/resources/day15.input");
             Scanner scanner = new Scanner(input)) {
            strs = scanner.nextLine();
        }

        List<String> strings = Arrays.asList(strs.split(","));
        IntStream hashes = strings.stream().mapToInt(Hashmap::hash);
        System.out.println(hashes.sum());
    }

}
