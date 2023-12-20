package day19

import scala.io.Source
import scala.annotation.tailrec

case class Rule(part: Char, op: Char, value: Int, destination: String) {
    def evaluate(rating: PartRating): Option[String] =
        op match
            case '<' => if rating(part) < value then Some(destination) else None
            case '>' => if rating(part) > value then Some(destination) else None

    def clamp(range: Range): Range =
        op match
            case '<' => range.copy(max = range.max.updated(part, value.min(range.max(part))))
            case '>' => range.copy(min = range.min.updated(part, value.max(range.min(part))))

    def clampInverse(range: Range): Range =
        op match
            case '<' => range.copy(min = range.min.updated(part, (value - 1).max(range.min(part))))
            case '>' => range.copy(max = range.max.updated(part, (value + 1).min(range.max(part))))
}

object Rule {
    def fromString(str: String): Rule =
        str match
            case s"$part<$value:$destination" => Rule(part(0), '<', value.toInt, destination)
            case s"$part>$value:$destination" => Rule(part(0), '>', value.toInt, destination)
}

case class Workflow(name: String, rules: IndexedSeq[Rule], terminal: String) {
    def consider(rating: PartRating): String =
        rules.map(_.evaluate(rating)).flatten.headOption.getOrElse(terminal)

    def countAccepted(workflows: Map[String, Workflow], range: Range): Long =
        val previousRulesApplied = rules.scanLeft(range)((range, rule) => rule.clampInverse(range))
        val rulesWithTerminal = rules :+ Rule('x', '>', 0, terminal)
        previousRulesApplied.zip(rulesWithTerminal)
                .map((range, rule) => (rule.clamp(range), rule.destination))
                .map((range, destination) => destination match
                    case "A" => range.product
                    case "R" => 0
                    case _ => workflows(destination).countAccepted(workflows, range)
                )
                .sum
}

object Workflow {
    def fromString(str: String): Workflow =
        str match
            case s"$name{$ruleset}" =>
                val rules = ruleset.split(',')
                val terminal = rules.last
                Workflow(name, rules.init.map(Rule.fromString), terminal)
}

case class PartRating(ratings: Map[Char, Int]) {
    def apply(ch: Char): Int = ratings(ch)

    def rate(workflows: Map[String, Workflow]): Int = 
        rate(workflows, workflows("in"))

    @tailrec
    private def rate(workflows: Map[String, Workflow], workflow: Workflow): Int =
        workflow.consider(this) match
            case "A" => ratings.values.sum
            case "R" => 0
            case next => rate(workflows, workflows(next))
}

object PartRating {
    def fromString(str: String): PartRating =
        str match
            case s"{x=$x,m=$m,a=$a,s=$s}" => PartRating(Map(('x', x.toInt), ('m', m.toInt), ('a', a.toInt), ('s', s.toInt)))
}

case class Range(min: Map[Char, Int], max: Map[Char, Int]) {
    def countAccepted(workflows: Map[String, Workflow]): Long =
        workflows("in").countAccepted(workflows, this)

    def product: Long = min.keys.toList.map(ch => max(ch) - min(ch) - 1L).product
}

@main def part37: Unit =
    val input = Source.fromResource("day19.input").getLines();
    val (workflowStrs, ratingStrs) = input.span(!_.isBlank)
    val workflows = workflowStrs.map(Workflow.fromString).map(w => w.name -> w).toMap
    val ratings = ratingStrs.drop(1).map(PartRating.fromString)
    val rated = ratings.map(_.rate(workflows))
    println(rated.sum)

@main def part38: Unit =
    val input = Source.fromResource("day19.input").getLines();
    val workflowStrs = input.takeWhile(!_.isBlank)
    val workflows = workflowStrs.map(Workflow.fromString).map(w => w.name -> w).toMap
    val startRange = Range(
        (for ch <- "xmas" yield ch -> 0).toMap,
        (for ch <- "xmas" yield ch -> 4001).toMap
    )
    println(startRange.countAccepted(workflows))
    //9223372036854775807
    //167409079868000
    //66793548869920

