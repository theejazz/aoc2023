package day7;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Part14 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream input = Part14.class.getClassLoader().getResourceAsStream("day7.input");
                Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }

        List<Hand> hands = lines.stream().map(line -> Hand.fromString(line, Hand.JCard.JOKER))
                .sorted(Collections.reverseOrder()).toList();

        long score = 0;
        for (int idx = 0; idx != hands.size(); ++idx) {
            score += (long) (idx + 1) * hands.get(idx).bid;
        }
        System.out.println(score);
    }

}
