import scala.io.Source
import scala.annotation.tailrec
import scala.util.matching.Regex

object DigitParser {

    val digitMap = Map(
        "one"   -> "1", 
        "two"   -> "2",
        "three" -> "3",
        "four"  -> "4",
        "five"  -> "5",
        "six"   -> "6",
        "seven" -> "7",
        "eight" -> "8",
        "nine"  -> "9"
    )

    def parseDigit(str: String): String =
        str.length match 
            case 1 => str
            case _ => digitMap(str)

    // N.b. onesevenine becomes 19
    def parseStringOfDigits(str: String): String =
        val regex = Regex("(?=(" + digitMap.keySet.mkString("|") + "|[1-9]))");
        val matches = regex.findAllMatchIn(str).map(_.group(1)).toVector;
        parseDigit(matches.head) + parseDigit(matches.last)
}

@main def part1(): Unit =
    val input = Source.fromResource("day1.input").getLines();
    val numbersOnly = input.map(_.filter(Character.isDigit))
    val firstAndLastDigits = numbersOnly.map(cs => "" + cs.head + cs.last)
    val sum = firstAndLastDigits.map(_.toInt).sum
    println(sum)

@main def part2(): Unit =
    val input = Source.fromResource("day1.input").getLines().toList;
    val numbersOnly = input.map(DigitParser.parseStringOfDigits)
    val firstAndLastDigits = numbersOnly.map(cs => "" + cs.head + cs.last)
    val sum = firstAndLastDigits.map(_.toInt).sum
    println(sum)
