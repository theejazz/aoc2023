package day9

import scala.io.Source

case class History(vals: List[Int]) {

    def predict: Int = 
        val newList = vals.sliding(2).map(xs => xs(1) - xs(0)).toList
        if (newList.filterNot(_ == 0).isEmpty) 
        then vals.last
        else History(newList).predict + vals.last

    def predictFirst: Int = 
        val newList = vals.sliding(2).map(xs => xs(1) - xs(0)).toList
        if (newList.filterNot(_ == 0).isEmpty) 
        then vals.head - 0
        else vals.head - History(newList).predictFirst

}

object History {
    def fromString(str: String): History =
        History(str.split(" ").map(_.toInt).toList)
}

@main def part17(): Unit =
    val input = Source.fromResource("day9.input").getLines().toList
    val histories = input.map(History.fromString)
    val nextValues = histories.map(_.predict)
    println(nextValues)
    println(nextValues.sum)

@main def part18(): Unit =
    val input = Source.fromResource("day9.input").getLines().toList
    val histories = input.map(History.fromString)
    val nextValues = histories.map(_.predictFirst)
    println(nextValues)
    println(nextValues.sum)