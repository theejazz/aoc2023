package day7;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class Hand implements Comparable<Hand> {

    enum JCard {
        JACK(11),
        JOKER(1);

        int value;

        private JCard(int value) {
            this.value = value;
        }
    };

    enum HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    };

    HandType type;
    List<Character> cards;
    int bid;
    JCard jcard;

    Hand(List<Character> cards, int bid, JCard jcard) {
        this.cards = cards;
        this.bid = bid;
        this.jcard = jcard;
        type = determineType();
    }

    HandType determineType() {
        Map<Character, Long> map = cards.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (jcard == JCard.JOKER && map.containsKey('J')) {
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

    static Hand fromString(String str, JCard jcard) {
        String[] words = str.split("\s+");
        List<Character> cards = words[0].chars().mapToObj(e -> (char) e).toList();
        int bid = Integer.parseInt(words[1]);
        return new Hand(cards, bid, jcard);
    }

    int rankCard(char ch) {
        return switch (ch) {
            case 'A' -> 14;
            case 'K' -> 13;
            case 'Q' -> 12;
            case 'J' -> this.jcard.value;
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