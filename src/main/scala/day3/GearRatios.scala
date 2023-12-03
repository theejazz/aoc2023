package day3

import scala.io.Source

case class Token(num: String, x: Int, y: Int) {

    def toInt = num.toInt

    def getBorder(): IndexedSeq[(Int, Int)] = 
        val coords = for {
            x <- x - 1 to x + num.size
            y <- y - 1 to y + 1
        } yield (x, y)

        coords.diff({ for x <- x until x + num.size yield (x, y) })

    def isPartNumber(schematic: Seq[String]): Boolean =
        if (num.toIntOption.isEmpty) {
            return false;
        }

        val coords = getBorder();        
        !coords.map((x, y) => Token.isSymbol(schematic, x, y)).filter(_ == true).isEmpty

    def findAdjacents(others: Seq[Token]): Seq[Token] = 
        val limitedRows = others.filter(other => { for y <- y - 1 to y + 1 yield y }.contains(other.y));
        limitedRows.filter(other => other.getBorder().contains((x, y)))
}

object Token {
    def isSymbol(schematic: Seq[String], x: Int, y: Int): Boolean =
        val lineLength = schematic(0).length
        val nLines = schematic.length
        (x, y) match
            case (-1, _) => false
            case (x, _) if x == lineLength => false
            case (_, -1) => false
            case (_, y) if y == nLines => false
            case (x, y) => isSymbol(schematic(y)(x))

    def isSymbol(ch: Char): Boolean = 
        !"0123456789.".contains(ch)
}

@main def part5(): Unit =
    val input = Source.fromResource("day3.input").getLines().toSeq;
    val numbers = input.zipWithIndex.map((str, i) => {
        val matches = "[0-9]+".r.findAllMatchIn(str).toSeq;
        matches.map(m => Token(m.matched, m.start, i))
    }).flatten;
    val partNumbers = numbers.filter(_.isPartNumber(input))
    println(partNumbers.map(_.toInt).sum)

@main def part6(): Unit =
    val input = Source.fromResource("day3.input").getLines().toSeq;
    val numbers = input.zipWithIndex.map((str, i) => {
        val matches = "[0-9]+".r.findAllMatchIn(str).toSeq;
        matches.map(m => Token(m.matched, m.start, i))
    }).flatten;

    val stars = input.zipWithIndex.map((str, i) => {
        val matches = "[*]".r.findAllMatchIn(str).toSeq;
        matches.map(m => Token(m.matched, m.start, i))
    }).flatten;

    val adjacents = stars.map(_.findAdjacents(numbers));

    println(adjacents.filter(_.length == 2).map(_.map(_.toInt).reduce(_ * _)).sum)
