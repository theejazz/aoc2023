package day11;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Part21 {

    public static void main(String[] args) throws IOException {
        List<String> image = new ArrayList<>();
        try (InputStream input = Part21.class.getClassLoader().getResourceAsStream("day11.input");
             Scanner scanner = new Scanner(input)
        ) {
            while (scanner.hasNextLine()) {
                image.add(scanner.nextLine());
            }
        }

        List<Integer> emptyRows = new ArrayList<>();
        for (int ydx = 0; ydx != image.size(); ++ydx) {
            if (image.get(ydx).indexOf('#') == -1) {
                emptyRows.add(ydx);
            }
        }

        List<Galaxy> galaxies = new ArrayList<>();
        List<Integer> emptyColumns = new ArrayList<>();
        for (int xdx = 0; xdx != image.get(0).length(); ++xdx) {
            boolean emptySpace = true;
            for (int ydx = 0; ydx != image.size(); ++ydx) {
                if (image.get(ydx).charAt(xdx) == '#') {
                    emptySpace = false;
                    galaxies.add(new Galaxy(xdx, ydx));
                }
            }

            if (emptySpace) {
                emptyColumns.add(xdx);
            }
        }

        long lengthSum = 0;
        for (int idx = 0; idx != galaxies.size(); ++idx) {
            for (int jdx = idx + 1; jdx != galaxies.size(); ++jdx) {
                lengthSum += galaxies.get(idx).calcSpaceBetween(galaxies.get(jdx), emptyRows, emptyColumns, 2);
            }
        }

        System.out.println(lengthSum);
    }

}
