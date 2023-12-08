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

    static long walk(Map<String, Node> map, String startingNode, String endNodeRegex, String directions) {
        List<Character> steps = directions.chars().mapToObj(ch -> (char) ch).toList();
        long stepCount = 0;

        Map<String, String> mapping = map.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        for (char step : steps) {
            for (String key : mapping.keySet()) {
                if (step == 'L') {
                    mapping.put(key, map.get(mapping.get(key)).left);
                } else if (step == 'R') {
                    mapping.put(key, map.get(mapping.get(key)).right);
                }
            }
        }

        String currentNode = startingNode;
        while (true) {
            currentNode = mapping.get(currentNode);

            stepCount += steps.size();

            if (currentNode.matches(endNodeRegex)) {
                break;
            }
        }

        return stepCount;
    }

    static long ghostWalk(Map<String, Node> map, String startingNodeRegex, String endNodeRegex, String directions) {
        List<String> startingNodes = map.keySet().stream().filter(str -> str.matches(startingNodeRegex)).toList();
        List<Long> walkLengths = startingNodes.stream().map(node -> walk(map, node, endNodeRegex, directions)).toList();
        // The problem really doesn't indicate that LCM is possible here but the input supports it...
        long stepCount = walkLengths.stream().reduce(Node::lcm).get();
        return stepCount;
    }

    static long lcm(long l1, long l2) {
        long higherNumber = Math.max(l1, l2);
        long lowerNumber = Math.min(l1, l2);
        long lcm = higherNumber;
        while (lcm % lowerNumber != 0) {
            lcm += higherNumber;
        }

        return lcm;
    }

}
