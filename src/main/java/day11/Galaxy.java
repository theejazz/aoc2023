package day11;

import java.util.List;

record Galaxy(int x, int y) {

    int calcSpaceBetween(Galaxy other, List<Integer> emptyRows, List<Integer> emptyColumns, int expansionFactor) {
        return calcVerticalDifference(other, emptyRows, expansionFactor) + calcHorizontalDifference(other, emptyColumns, expansionFactor);
    }

    int calcVerticalDifference(Galaxy other, List<Integer> emptyRows, int expansionFactor) {
        int smallestY = Math.min(this.y, other.y);
        int largestY = Math.max(this.y, other.y);
        return largestY - smallestY + (int) emptyRows.stream().dropWhile(r -> r < smallestY).takeWhile(r -> r < largestY).count() * (expansionFactor - 1);
    }

    int calcHorizontalDifference(Galaxy other, List<Integer> emptyColumns, int expansionFactor) {
        int smallestX = Math.min(this.x, other.x);
        int largestX = Math.max(this.x, other.x);
        return largestX - smallestX + (int) emptyColumns.stream().dropWhile(c -> c < smallestX).takeWhile(c -> c < largestX).count() * (expansionFactor - 1);
    }

}
