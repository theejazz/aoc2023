package day8;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class Node {

    String name;
    String left;
    String right;

    Node(String name, String left, String right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    static Node fromString(String str) {
        String cleaned = str.replaceAll("[ =(,)]", "");
        return new Node(cleaned.substring(0, 3), cleaned.substring(3, 6), cleaned.substring(6));
    }

    static long walk(Map<String, Node> map, String directions) {
        List<Character> steps = directions.chars().mapToObj(ch -> (char) ch).toList();
        long stepCount = 0;
        String currentNode = "AAA";
        for (int idx = 0; idx != 1000; ++idx) {
            for (char step : steps) {
                if (step == 'L') {
                    currentNode = map.get(currentNode).left;
                } else if (step == 'R') {
                    currentNode = map.get(currentNode).right;
                }
            }

            stepCount += steps.size();

            if ("ZZZ".equals(currentNode)) {
                break;
            }
        }

        return stepCount;
    }

    static long ghostWalk(Map<String, Node> map, String directions) {
        List<Character> steps = directions.chars().mapToObj(ch -> (char) ch).toList();
        long stepCount = 0;

        // Create a mapping from every node to the end result after following all directions
        Map<String, String> mapping = map.keySet().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
        for (char step : steps) {
            for (String key : mapping.keySet()) {
                if (step == 'L') {
                    mapping.put(key, map.get(mapping.get(key)).left);
                } else if (step == 'R') {
                    mapping.put(key, map.get(mapping.get(key)).right);
                }
            }
        }

        List<String> paths = map.keySet().stream().filter(str -> str.endsWith("A")).toList();
        while (true) {
            paths = paths.stream().map(mapping::get).toList();

            stepCount += steps.size();


            if (paths.stream().allMatch(str -> str.endsWith("Z"))) {
                break;

            }
        }

        return stepCount;
    }

}
