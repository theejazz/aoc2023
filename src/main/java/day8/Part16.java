package day8;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Part16 {

    public static void main(String[] args) throws IOException {
        String directions;
        List<String> nodes = new ArrayList<>();
        try (InputStream input = Part16.class.getClassLoader().getResourceAsStream("day8.input");
             Scanner scanner = new Scanner(input)
        ) {
            directions = scanner.nextLine();
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                nodes.add(scanner.nextLine());
            }
        }

        Map<String, Node> map = nodes.stream().map(Node::fromString).collect(Collectors.toMap(node -> node.name, Function.identity()));
        System.out.println(Node.ghostWalk(map, "..A", "..Z", directions));
    }

}
