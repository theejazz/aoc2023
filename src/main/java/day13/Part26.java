package day13;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

class Part26 {

    public static void main(String[] args) throws IOException {
        List<MirrorGrid> grids = MirrorGrid.readGrids("src/main/resources/day13.input");
        IntStream scores = grids.stream().mapToInt(g -> g.findMirrorWithSmudges(1));
        System.out.println(scores.sum());
    }

}
