package day7;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Part14 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream input = Part14.class.getClassLoader().getResourceAsStream("day7.input");
             Scanner scanner = new Scanner(input)
        ) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }

        List<Hand> hands = lines.stream().map(Hand::fromString).sorted(Collections.reverseOrder()).toList();

        long score = 0;
        for (int idx = 0; idx != hands.size(); ++idx) {
            score += (long) (idx + 1) * hands.get(idx).bid;
        }
        System.out.println(score);
    }

}


class Hand implements Comparable<Hand> {

    enum HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    HandType type;
    List<Character> cards;
    int bid;

    public Hand(List<Character> cards, int bid) {
        this.cards = cards;
        this.bid = bid;
        type = determineType();
    }

    HandType determineType() {
        Map<Character, Long> map = cards.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (map.containsKey('J')) {
            long jCount = map.get('J');
            map.remove('J');
            if (map.isEmpty())
                return HandType.FIVE_OF_A_KIND;

            char meestVoorkomend = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
            map.merge(meestVoorkomend, jCount, Long::sum);
        }

        return switch (map.size()) {
            case 1 -> HandType.FIVE_OF_A_KIND;
            case 2 -> map.containsValue(4L) ? HandType.FOUR_OF_A_KIND : HandType.FULL_HOUSE;
            case 3 -> map.containsValue(3L) ? HandType.THREE_OF_A_KIND : HandType.TWO_PAIR;
            case 4 -> HandType.ONE_PAIR;
            default -> HandType.HIGH_CARD;
        };
    }

    static Hand fromString(String str) {
        String[] words = str.split("\s+");
        List<Character> cards = words[0].chars().mapToObj(e -> (char) e).toList();
        int bid = Integer.parseInt(words[1]);
        return new Hand(cards, bid);
    }

    static Integer rankCard(char ch) {
        return switch(ch) {
            case 'A' -> 14;
            case 'K' -> 13;
            case 'Q' -> 12;
            case 'J' -> 1;
            case 'T' -> 10;
            default -> Integer.parseInt("" + ch);
        };
    }

    @Override
    public int compareTo(Hand o) {
        if (type != o.type) {
            return type.compareTo(o.type);
        }

        for (int idx = 0; idx != cards.size(); ++idx) {
            if (cards.get(idx).equals(o.cards.get(idx)))
                continue;

            return Integer.compare(rankCard(o.cards.get(idx)), rankCard(cards.get(idx)));
        }

        return 0;
    }
}