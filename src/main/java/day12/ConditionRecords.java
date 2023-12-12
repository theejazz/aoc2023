package day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionRecords {

    static List<String> determineSolutions(String rec) {
        List<String> solutions = new ArrayList<>();
        String[] splitInput = rec.split(" ");
        String pattern = splitInput[0];
        List<Integer> numbers = Arrays.stream(splitInput[1].split(",")).map(Integer::parseInt).toList();

        solutions = makeSolution(pattern, ".".repeat(pattern.length()), numbers);

        return solutions;
    }

    static List<String> makeSolution(String rec, String current, List<Integer> numbers) {
        if (numbers.isEmpty()) {
            if (current.matches(rec.replaceAll("\\.", "\\\\\\.").replace('?', '.'))) {
                return List.of(padRight(current, rec.length(), '.'));
            } else {
                return List.of();
            }
        }

        List<List<String>> newSolutions = new ArrayList<>();
        int length = numbers.get(0);
        int nextChar = current.lastIndexOf('#') == -1 ? current.lastIndexOf('#') + 1 : current.lastIndexOf('#') + 2;
        for (int idx = nextChar; idx <= limit(rec, current.lastIndexOf('#'), numbers); ++idx) {
            String currentMutated = padRight(current.substring(0, idx) + "#".repeat(length), rec.length(), '.');
            if (currentMutated.substring(0, idx + length).matches(rec.substring(0, idx + length).replaceAll("\\.", "\\\\\\.").replace('?', '.'))) {
                newSolutions.add(makeSolution(rec, currentMutated, numbers.subList(1, numbers.size())));
            }
        }

        return newSolutions.stream().flatMap(List::stream).toList();
    }

    static int limit(String rec, int idx, List<Integer> numbers) {
        return rec.length() - numbers.stream().mapToInt(i -> i + 1).sum() + 1;
    }

    static String padRight(String toPad, int length, char ch) {
        return String.format("%-" + length + "s", toPad).replace(' ', ch);
    }

}
