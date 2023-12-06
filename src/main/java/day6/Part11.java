package day6;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Part11 {

    public static void main(String[] args) throws IOException {
        try (InputStream input = Part11.class.getClassLoader().getResourceAsStream("day6.input");
             Scanner scanner = new Scanner(input)
        ) {
            List<Integer> times = Arrays.stream(scanner.nextLine().split("\s+")).skip(1).map(Integer::parseInt).toList();
            List<Integer> distance = Arrays.stream(scanner.nextLine().split("\s+")).skip(1).map(Integer::parseInt).toList();

            int multiple = 1;
            for (int idx = 0; idx != times.size(); ++idx) {
                int wins = 0;
                int time = times.get(idx);
                for (int wait = 0; wait != time; ++wait) {
                    if (wait * (time - wait) > distance.get(idx)) {
                        ++wins;
                    }
                }
                multiple *= wins;
            }

            System.out.println(multiple);
        }
    }

}
