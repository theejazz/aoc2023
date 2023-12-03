import scala.io.Source
import scala.compiletime.ops.boolean

enum Colours { case BLUE, GREEN, RED }

case class Game(blue: Int, green: Int, red: Int) {
    def canBePlayedWith(other: Game): Boolean =
        this.blue <= other.blue && this.green <= other.green && this.red <= other.red

    def power: Int =
        this.blue * this.green * this.red;
}

object Game {
    def fromString(str: String): Game =
        val filtered = str.replaceFirst("Game [0-9]+: ", "");
        val turns = filtered.split("[;,] ");
        val colours = turns.map(colourFromString);
        val grouped = colours.groupBy(_._1);
        val max = grouped.mapValues(arr => arr.map(_._2).max);
        Game(max.getOrElse(Colours.BLUE, 0), max.getOrElse(Colours.GREEN, 0), max.getOrElse(Colours.RED, 0));

    def colourFromString(str: String): (Colours, Int) =
        str match
            case s"$x blue"     if x.toIntOption.isDefined  => (Colours.BLUE, x.toInt)
            case s"$x green"    if x.toIntOption.isDefined  => (Colours.GREEN, x.toInt)
            case s"$x red"      if x.toIntOption.isDefined  => (Colours.RED, x.toInt)
            case _ => throw new UnsupportedOperationException("Unknown colour")
}

@main def part3(): Unit =
    val input = Source.fromResource("day2.input").getLines.toVector;
    val games = input.map(Game.fromString).zipWithIndex;
    val valid = games.filter((game, _) => game.canBePlayedWith(Game(14, 13, 12)))
    println(valid.map(_._2 + 1).sum)

@main def part4(): Unit =
    val input = Source.fromResource("day2.input").getLines.toVector;
    val games = input.map(Game.fromString);
    val powers = games.map(_.power)
    println(powers.sum)