package day5

import scala.io.Source

case class Mapping(destination: Long, source: Long, length: Long) {
    def containsSource(value: Long): Boolean =
        value >= source && value < source + length

    def mapToDestination(value: Long): Long = 
        if containsSource(value) then value - source + destination else value
}

case class Map(mappings: IndexedSeq[Mapping]) {
    def mapToDestination(value: Long): Long =
        val differentDestinations = mappings.map(_.mapToDestination(value)).filter(_ != value)
        if differentDestinations.isEmpty then value else differentDestinations(0)
}

object Almanac {
    def seedsFromString(str: String): IndexedSeq[Long] =
        str match
            case s"seeds: $n" => n.split(" ").map(_.toLong)
            case _ => throw new UnsupportedOperationException

    def seedsWithRangeFromString(str: String): Iterator[Long] =
        str match
            case s"seeds: $n" => 
                n.split(" ").map(_.toLong).sliding(2, 2).map(arr => arr(0) to arr(1)).flatten
        
    def mapFromString(str: String): Map =
        def fromString(str: String): Mapping =
            val vals = str.split(" ").map(_.toLong)
            Mapping(vals(0), vals(1), vals(2))
        
        Map(str.split("\n").tail.map(fromString))
}

@main def part9(): Unit =
    val input = Source.fromResource("day5.input").mkString.split("\n\n")
    val seeds = Almanac.seedsFromString(input.head)
    val maps = input.tail.map(Almanac.mapFromString)
    val locations = seeds.map(seed => maps.foldLeft(seed){(s, m) => m.mapToDestination(s)})
    println(locations.min)

@main def part10(): Unit =
    val input = Source.fromResource("day5.input").mkString.split("\n\n")
    val seeds = Almanac.seedsWithRangeFromString(input.head)
    val maps = input.tail.map(Almanac.mapFromString)
    val locations = seeds.map(seed => maps.foldLeft(seed){(s, m) => m.mapToDestination(s)})
    println(locations.min)
