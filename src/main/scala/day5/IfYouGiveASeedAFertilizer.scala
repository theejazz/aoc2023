package day5

import scala.io.Source

case class Mapping(destination: Long, source: Long, length: Long) {
    def containsSource(value: Long): Boolean =
        value >= source && value < source + length

    def mapToDestination(value: Long): Long = 
        if containsSource(value) then value - source + destination else value

    def mapChange(range: (Long, Long)): ((Long, Long), (Long, Long)) = 
        val end = source + length
        range match
            case (x, y) if x >= end => null
            case (x, y) if y <= source => null
            case (x, y) if x >= source && y < end => 
                ((x, y), (destination + x - source, destination + y - source))
            case (x, y) if x >= source && y >= end => 
                ((x, source + length), (destination + x - source, destination + length))
            case (x, y) if x < source && y < end => 
                ((source, y), (destination, destination + y - source))
            case (x, y) if x < source && y >= end => 
                ((source, source + length), (destination, destination + length))
}

case class Map(mappings: IndexedSeq[Mapping]) {
    def mapToDestination(value: Long): Long =
        val differentDestinations = mappings.map(_.mapToDestination(value)).filter(_ != value)
        if differentDestinations.isEmpty then value else differentDestinations(0)

    def mapToDestination(ranges: IndexedSeq[(Long, Long)]): IndexedSeq[(Long, Long)] =
        def determineUnchanged(range: (Long, Long), changed: IndexedSeq[(Long, Long)]): IndexedSeq[(Long, Long)] =
            changed: IndexedSeq[(Long, Long)] match
                case xs if xs.isEmpty => IndexedSeq()
                case x+:xs if x._1 == range._1 => determineUnchanged((range._2.min(x._2), range._2), xs)
                case x+:xs => (range._1, range._2.min(x._2)) +: determineUnchanged((range._2.min(x._2), range._2), xs)

        def mapToDestination(range: (Long, Long)): Iterator[(Long, Long)] =
            val changes = mappings.map(_.mapChange(range)).filterNot(_ == null)
            val unchanged = if changes.isEmpty then List(range) else determineUnchanged(range, changes.map(_._1).sortBy(_._1))
            (changes.map(_._2) ++ unchanged).iterator

        ranges.map(mapToDestination).flatten
}

object Almanac {
    def seedsFromString(str: String): IndexedSeq[Long] =
        str match
            case s"seeds: $n" => n.split(" ").map(_.toLong)
            case _ => throw new UnsupportedOperationException

    def seedRangesFromString(str: String): Iterator[(Long, Long)] =
        str match
            case s"seeds: $n" => 
                n.split(" ").map(_.toLong).sliding(2, 2).map(arr => (arr(0), arr(0) + arr(1)))

        
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
    val seedRanges = Almanac.seedRangesFromString(input.head).toIndexedSeq
    val maps = input.tail.map(Almanac.mapFromString)
    val locations = maps.foldLeft(seedRanges){ (ranges, map) => map.mapToDestination(ranges) }
    println(locations.minBy(_._1)._1)