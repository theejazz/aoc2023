package day12;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

record ConditionRecord(String rec, List<Integer> numbers) {

    static String debugString;

    static Map<String, Long> cache = new HashMap<>();

    static ConditionRecord fromString(String str) {
        return fromString(str, 1);
    }

    static ConditionRecord fromString(String str, int foldFactor) {
        String[] split = str.split(" ");
        String rec = Collections.nCopies(foldFactor, split[0]).stream().collect(Collectors.joining("?"));
        List<Integer> numbers = Arrays.asList(Collections
                .nCopies(foldFactor, split[1])
                .stream()
                .collect(Collectors.joining(","))
                .split(","))
                .stream()
                .map(Integer::parseInt)
                .toList();
        return new ConditionRecord(rec, numbers);
    }

    long findNumberOfSolutions() {
        debugString = rec;
        return findNumberOfSolutions(true);
    }

    long findNumberOfSolutions(boolean reset) {
        if (reset) {
            cache = new HashMap<>();
        }

        if (rec.isEmpty()) {
            return numbers.isEmpty() ? 1 : 0;
        }

        String hash = this.toHash();
        if (cache.containsKey(hash)) {
            return cache.get(hash);
        }

        long nSolutions = switch (rec.charAt(0)) {
            case '.' -> findNumberOfSolutionsStartWithDot();
            case '#' -> findNumberOfSolutionsStartWithStar();
            case '?' -> findNumberOfSolutionsStartWithDot() + findNumberOfSolutionsStartWithStar();
            default -> throw new UnsupportedOperationException("Rec is: " + rec);
        };

        cache.put(hash, nSolutions);
        return nSolutions;
    }

    long findNumberOfSolutionsStartWithDot() {
        return new ConditionRecord(rec.substring(1), numbers)
                .findNumberOfSolutions(false);
    }

    long findNumberOfSolutionsStartWithStar() {
        if (numbers.isEmpty()) {
            return 0;
        }

        int nextLength = numbers.get(0);
        if (rec.length() < nextLength) {
            return 0;
        }

        if (rec.substring(0, nextLength).matches("^[#\\?]{" + nextLength + "}")) {
            if (rec.length() == nextLength) {
                return numbers.size() == 1 ? 1 : 0;
            } else if (rec.charAt(nextLength) == '#') {
                return 0;
            } else {
                return new ConditionRecord(rec.substring(nextLength + 1), numbers.subList(1, numbers.size()))
                        .findNumberOfSolutions(false);
            }
        } else {
            return 0;
        }
    }

    String toHash() {
        return rec + numbers.size();
    }

}
