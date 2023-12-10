package day10;

import java.util.ArrayList;
import java.util.List;

class Pipes {

    static List<Point> makeLoop(List<String> lines) {
        List<Point> loop = new ArrayList<>();
        Point current = null;
        Point previous = null;
        for (int idx = 0; idx != lines.size(); ++idx) {
            String line = lines.get(idx);
            int jdx = line.indexOf('S');
            if (jdx != -1) {
                previous = new Point(jdx, idx, 'S');
                loop.add(previous);
                // Assume 'S' is not at the border
                if (List.of('-', 'L', 'F').contains(line.charAt(jdx - 1))) {
                    current = Point.make(lines, jdx - 1, idx);
                } else if (List.of('-', 'J', '7').contains(line.charAt(jdx + 1))) {
                    current = Point.make(lines, jdx + 1, idx);
                } else {
                    current = Point.make(lines, jdx, idx + 1);
                }

                break;
            }
        }

        assert previous != null;

        while (current.c() != 'S') {
            loop.add(current);
            Point tmp = current;
            current = findNextChar(lines, previous, current);
            previous = tmp;
        }

        return loop;
    }

    static Point findNextChar(List<String> lines, Point p, Point c) {
        return switch (c.c()) {
            case '|' -> p.y() < c.y() ? Point.make(lines, c.x(), c.y() + 1) : Point.make(lines, c.x(), c.y() - 1);
            case '-' -> p.x() < c.x() ? Point.make(lines, c.x() + 1, c.y()) : Point.make(lines, c.x() - 1, c.y());
            case 'L' -> p.x() > c.x() ? Point.make(lines, c.x(), c.y() - 1) : Point.make(lines, c.x() + 1, c.y());
            case 'J' -> p.x() < c.x() ? Point.make(lines, c.x(), c.y() - 1) : Point.make(lines, c.x() - 1, c.y());
            case '7' -> p.x() < c.x() ? Point.make(lines, c.x(), c.y() + 1) : Point.make(lines, c.x() - 1, c.y());
            case 'F' -> p.x() > c.x() ? Point.make(lines, c.x(), c.y() + 1) : Point.make(lines, c.x() + 1, c.y());
            default -> throw new UnsupportedOperationException();
        };
    }

    static List<Point> findEnclosed(List<String> lines, List<Point> loop) {
        List<Point> enclosed = new ArrayList<>();
        for (int ydx = 0; ydx != lines.size(); ++ydx) {
            for (int xdx = 0; xdx != lines.get(0).length(); ++xdx) {
                Point now = Point.make(lines, xdx, ydx);
                if (!loop.contains(now) && evenOddRule(lines, loop, now) % 2 == 1) {
                    enclosed.add(now);
                }
            }
        }

        return enclosed;
    }

    static long evenOddRule(List<String> lines, List<Point> loop, Point now) {
        String line = lines.get(now.y());
        List<Point> onLine = loop.stream()
                .filter(p -> p.y() == now.y())
                .filter(p -> p.c() != '-')
                .toList();

        int sIndex = line.indexOf('S');
        onLine = (sIndex == -1 || sIndex < now.x())
                ? onLine.stream().filter(p -> p.x() > now.x()).toList()
                : onLine.stream().filter(p -> p.x() < now.x()).toList();

        long windingNumber = 0;
        windingNumber += onLine.stream().filter(p -> p.c() == '|').count();
        onLine = onLine.stream().filter(p -> !List.of('|').contains(p.c())).toList();

        assert onLine.size() % 2 == 0;

        for (int idx = 0; idx < onLine.size(); idx += 2) {
            List<Character> nextTwoPipes = onLine.subList(idx, idx + 2).stream().map(Point::c).toList();
            if (!(List.of('L', 'J').containsAll(nextTwoPipes) || List.of('F', '7').containsAll(nextTwoPipes))) {
                ++windingNumber;
            }
        }

        return windingNumber;
    }
}

record Point(int x, int y, char c) {

    static Point make(List<String> lines, int x, int y) {

        return new Point(x, y, lines.get(y).charAt(x));
    }

};
