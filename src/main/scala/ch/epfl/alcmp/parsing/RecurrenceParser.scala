package ch.epfl.alcmp.parsing

import scala.util.parsing.combinator._

class RecurrenceParser extends RegexParsers {

  def functionDeclaration: Parser[FunctionDeclaration] = char ~ "(" ~ char ~ ")" ^^ {
    case f ~ "(" ~ x ~ ")" => FunctionDeclaration(f, x)
  }

  def linear1: Parser[LinearTerm] = number ~ char ^^ {
    case n ~ c => LinearTerm(c, n)
  }

  def linear2: Parser[LinearTerm] = (number <~ "/") ~ number ~ char ^^ {
    case n ~ d ~ c => LinearTerm(c, n.doubleValue / d)
  }

  def linear3: Parser[LinearTerm] = number ~ (char <~ "/") ~ number ^^ {
    case n ~ c ~ d => LinearTerm(c, n.doubleValue / d)
  }

  def linear4: Parser[LinearTerm] = (char <~ "/") ~ number ^^ {
    case c ~ d => LinearTerm(c, 1.0 / d)
  }

  def linearTerm: Parser[LinearTerm] = linear4 | linear3 | linear2 | linear1


  def pureFunctionCall: Parser[FunctionCall] = (char <~ "(") ~ (linearTerm <~ ")") ^^ {
    case f ~ term => FunctionCall(f, term.variable, 1, term.factor)
  }

  def weightedFunctionCall: Parser[FunctionCall] = number ~ pureFunctionCall ^^ {
    case n ~ call => FunctionCall(call.name, call.variable, n, call.innerFactor)
  }

  def functionCall: Parser[FunctionCall] = weightedFunctionCall | pureFunctionCall

  def number: Parser[Int] = """\d+""".r ^^ {x => x.toInt}
  def char: Parser[Char] = """[a-zA-Z]""".r ^^ {_.charAt(0)}

  def expression: Parser[Sum] = sumExpression

  def sumExpression: Parser[Sum] = (functionCall | complexityExpression) ~ rep("+" ~> (functionCall | complexityExpression)) ^^ {
    case firstTerm ~ otherTerms => Sum(firstTerm, otherTerms)
  }

  def complexityExpression: Parser[ComplexityExpression] = "O(" ~ (polynomial1 | polynomial2) ~ ")" ^^ {
    case _ ~ expr ~ _ => ComplexityExpression(expr)
  }

  def polynomial1: Parser[Polynomial] = char ~ "^" ~ number ^^ {
    case c ~ "^" ~ n => Polynomial(c, n)
  }
  def polynomial2: Parser[Polynomial] = ("1" | char) ^^ {
    case c: Char => Polynomial(c, 1)
    case _ => Polynomial('0', 0)
  }

  def equation: Parser[Equation] = functionDeclaration ~ "=" ~ expression ^^ {
    case a ~ "=" ~ b => Equation(a, b.firstTerm +: b.right)
  }
}

trait Expression

case class Polynomial(variable: Char, maxDegree: Int) extends Expression
case class ComplexityExpression(inner: Polynomial) extends OuterTerm
case class Equation(left: FunctionDeclaration, right: List[OuterTerm])
trait OuterTerm extends Expression
case class Sum(firstTerm: OuterTerm, right: List[OuterTerm]) extends Expression
case class FunctionDeclaration(name: Char, variable: Char) extends Expression
case class FunctionCall(name: Char, variable: Char, outerFactor: Double, innerFactor: Double) extends OuterTerm
case class LinearTerm(variable: Char, factor: Double) extends Expression

