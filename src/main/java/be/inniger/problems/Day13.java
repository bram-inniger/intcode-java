package be.inniger.problems;

import be.inniger.IntCode;
import be.inniger.IntCode.Status;
import be.inniger.util.Coordinate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day13 {

    public static long partOne(List<Long> program) {
        var game = new Game(program);
        game.play();

//        System.out.println(game.prettyPrint());

        return game.tiles.values().stream().filter(t -> t == Game.Tile.BLOCK).count();
    }

    private static class Game {

        private final IntCode intCode;
        private final Map<Coordinate, Tile> tiles;

        private Game(List<Long> program) {
            this.intCode = new IntCode(program);
            this.tiles = new HashMap<>();
        }

        private void play() {
            var status = intCode.run();

            if (status != Status.HALTED) {
                throw new UnsupportedOperationException();
            }

            while (!intCode.output().isEmpty()) {
                var x = intCode.output().remove();
                var y = intCode.output().remove();
                var id = intCode.output().remove();

                tiles.put(new Coordinate(x, y), Tile.of(id));
            }
        }

        @SuppressWarnings("unused")
        private String prettyPrint() {
            var minX = tiles.keySet().stream().mapToLong(Coordinate::x).min().orElseThrow();
            var maxX = tiles.keySet().stream().mapToLong(Coordinate::x).max().orElseThrow();
            var minY = tiles.keySet().stream().mapToLong(Coordinate::y).min().orElseThrow();
            var maxY = tiles.keySet().stream().mapToLong(Coordinate::y).max().orElseThrow();

            return LongStream.rangeClosed(minY, maxY)
                    .mapToObj(y -> LongStream.rangeClosed(minX, maxX)
                            .mapToObj(x -> tiles.getOrDefault(new Coordinate(x, y), Tile.EMPTY).display)
                            .collect(Collectors.joining())

                    )
                    .collect(Collectors.joining("\n"));
        }

        private enum Tile {
            //@formatter:off
            EMPTY(0, " "),
            WALL(1, "#"),
            BLOCK(2, "■"),
            PADDLE(3, "_"),
            BALL(4, "o"),
            ;
            //@formatter:on

            private final long id;
            private final String display;

            Tile(long id, String display) {
                this.id = id;
                this.display = display;
            }

            private static Tile of(long id) {
                return Arrays.stream(Tile.values()).filter(tile -> tile.id == id).findFirst().orElseThrow();
            }
        }
    }
}
