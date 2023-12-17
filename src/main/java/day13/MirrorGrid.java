package day13;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

record MirrorGrid(List<String> grid) {

    int findMirrorWithSmudges(int smudges) {
        Optional<Integer> horizontal = findHorizontalMirrorWithSmudges(smudges);
        if (horizontal.isPresent()) {
            return (horizontal.get() + 1) * 100;
        } else {
            Optional<Integer> vertical = findVerticalMirrorWithSmudges(smudges);
            if (vertical.isPresent()) {
                return vertical.get() + 1;
            } else {
                // debug
                throw new UnsupportedOperationException();
            }
        }
    }

    Optional<Integer> findHorizontalMirrorWithSmudges(int smudges) {
        for (int row = 1; row < grid.size(); row += 2) {
            int differences = 0;
            for (int idx = 0; idx <= (row) / 2; ++idx) {
                differences += countDifferencesBetweenRows(idx, row - idx);
                if (differences > smudges) {
                    break;
                }
            }

            if (differences == smudges) {
                return Optional.of((row - 1) / 2);
            }
        }

        for (int row = 1; row < grid.size() - 1; row += 2) {
            int differences = 0;
            for (int idx = 0; idx <= (row) / 2; ++idx) {
                differences += countDifferencesBetweenRows(grid.size() - idx - 1, grid.size() - row + idx - 1);
                if (differences > smudges) {
                    break;
                }
            }

            if (differences == smudges) {
                return Optional.of(grid.size() - (row + 1) / 2 - 1);
            }
        }

        return Optional.empty();
    }

    Optional<Integer> findVerticalMirrorWithSmudges(int smudges) {
        int width = grid.get(0).length();

        for (int column = 1; column < width; column += 2) {
            int differences = 0;
            for (int idx = 0; idx <= column / 2; ++idx) {
                differences += countDifferencesBetweenColumns(idx, column - idx);
                if (differences > smudges) {
                    break;
                }
            }

            if (differences == smudges) {
                return Optional.of((column - 1) / 2);
            }
        }

        for (int column = 1; column < width - 1; column += 2) {
            int differences = 0;
            for (int idx = 0; idx <= column / 2; ++idx) {
                differences += countDifferencesBetweenColumns(width - idx - 1, width - column + idx - 1);
                if (differences > smudges) {
                    break;
                }
            }

            if (differences == smudges) {
                return Optional.of(width - (column + 1) / 2 - 1);
            }
        }

        return Optional.empty();
    }

    int countDifferencesBetweenRows(int row1, int row2) {
        int differences = 0;
        int width = grid.get(row1).length();
        for (int idx = 0; idx != width; ++idx) {
            if (grid.get(row1).charAt(idx) != grid.get(row2).charAt(idx)) {
                ++differences;
            }
        }

        return differences;
    }

    int countDifferencesBetweenColumns(int column1, int column2) {
        int differences = 0;
        for (int ydx = 0; ydx != grid.size(); ++ydx) {
            if (grid.get(ydx).charAt(column1) != grid.get(ydx).charAt(column2)) {
                ++differences;
            }
        }

        return differences;
    }

    static List<MirrorGrid> readGrids(String file) throws IOException {
        List<MirrorGrid> grids = new ArrayList<>();
        try (InputStream input = new FileInputStream(file);
                Scanner scanner = new Scanner(input)) {
            List<String> grid = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) {
                    grids.add(new MirrorGrid(grid));
                    grid = new ArrayList<>();
                } else {
                    grid.add(line);
                }
            }

            grids.add(new MirrorGrid(grid));
        }

        return grids;
    }
}
