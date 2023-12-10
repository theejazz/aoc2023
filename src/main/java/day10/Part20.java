package day10;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Part20 {
  
    public static void main(String[] args) throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream input = new FileInputStream("src/main/resources/day10.input");
             Scanner scanner = new Scanner(input)
        ) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
            }
        }

        List<Point> loop = Pipes.makeLoop(lines);
        List<Point> enclosing = Pipes.findEnclosed(lines, loop);
        System.out.println(enclosing.size());
    }
}