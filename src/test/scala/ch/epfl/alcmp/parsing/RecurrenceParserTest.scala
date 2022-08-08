package ch.epfl.alcmp.parsing
import scala.util.parsing.combinator._
import scala.util.parsing.combinator.Parsers

object RecurrenceParserTest {

  def main(args: Array[String]): Unit = {
    val parser = new RecurrenceParser()

    parser.parseAll(parser.linearTerm, "3n/2") match {
      case parser.Success(result, _) => println(result)
      case _ => println("Failed to parse input")
    }

    parser.parseAll(parser.equation, "T(n)=6T(3n/2)+O(n)") match {
      case parser.Success(result, _) => println(result)
      case _ => println("Failed to parse input")
    }
  }
}
