package day6;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Part12 {

    public static void main(String[] args) throws IOException {
        try (InputStream input = Part12.class.getClassLoader().getResourceAsStream("day6.input");
             Scanner scanner = new Scanner(input)
        ) {
            long time = Long.parseLong(scanner.nextLine().split(":")[1].replaceAll("\s+", ""));
            long distance = Long.parseLong(scanner.nextLine().split(":")[1].replaceAll("\s+", ""));

            for (long wait = 0; wait != time / 2 + 1; ++wait) {
                if (wait * (time - wait) > distance) {
                    System.out.println(time - wait - wait + 1);
                    return;
                }
            }
        }
    }

}
