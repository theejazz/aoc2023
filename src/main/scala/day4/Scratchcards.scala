package day4

import scala.io.Source
import scala.collection.mutable.LinkedHashMap
import scala.collection.mutable.ArraySeq

case class Scratchcard(num: Int, winning: IndexedSeq[Int], numbersYouHave: IndexedSeq[Int]) {

    def score: Int =
        matches match
            case 0 => 0
            case x => 1 << (x - 1)

    def matches: Int =
        winning.intersect(numbersYouHave).size

}

object Scratchcards {

    def fromString(str: String): Scratchcard =
        str match
            case s"Card $n0: $n1 | $n2" =>
                val num = n0.strip.toInt
                val winning = n1.strip.split(" +").map(_.toInt)
                val numbers = n2.strip.split(" +").map(_.toInt)
                Scratchcard(num, winning, numbers)
            case _ => throw new UnsupportedOperationException

    // How would you do this in a functional style??
    def scratch(cards: IndexedSeq[Scratchcard]): Int =
        val scores = LazyList.from(1).zip(cards.map(_.matches)).force
        val counts = ArraySeq.fill(scores.size){ 1 }
        val vals = for (n, score) <- scores yield {
            for x <- n until n + score yield {
                if (n < counts.size) {
                    counts.update(x, counts(x) + counts(n - 1))
                }
            }
            counts(n - 1)
        }

        vals.sum
}

@main def part7(): Unit =
    val input = Source.fromResource("day4.input").getLines.toVector
    val scratchcards = input.map(Scratchcards.fromString)
    val score = scratchcards.map(_.score).sum
    println(score)
    
@main def part8(): Unit = 
    val input = Source.fromResource("day4.input").getLines.toVector
    val scratchcards = input.map(Scratchcards.fromString)
    println(Scratchcards.scratch(scratchcards))

