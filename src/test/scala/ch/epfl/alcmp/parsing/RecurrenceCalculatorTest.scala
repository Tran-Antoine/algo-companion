package ch.epfl.alcmp.parsing
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.*
import matchers.*

class RecurrenceCalculatorTest extends AnyFlatSpec with should.Matchers {

  "Calculating recursion-less expression" should "output the right side" in {

    val rec = "T(n)=O(n)"
    RecurrenceCalculator.complexity(rec) should be (Some("n", true))
  }

  "Calculating exact expressions" should "give the exact complexity" in {

    val rec0 = "T(n)=T(n/2)+O(n)"
    val rec1 = "T(n)=2T(n/2)+O(n)"
    val rec2 = rec1 + "+O(1)"
    val rec3 = "T(n)=T(n/2)+T(n/2)+O(n)"
    val rec4 = "T(n)=2T(n/2)+T(n/2)+O(n^2)"
    val rec5 = "T(n)=16T(n/4)+O(n^2)"
    
    RecurrenceCalculator.complexity(rec0) should be (Some("n", true))
    RecurrenceCalculator.complexity(rec1) should be (Some("nlog(n)", true))
    RecurrenceCalculator.complexity(rec2) should be (Some("nlog(n)", true))
    RecurrenceCalculator.complexity(rec3) should be (Some("nlog(n)", true))
    RecurrenceCalculator.complexity(rec4) should be (Some("n^2", true))
    RecurrenceCalculator.complexity(rec5) should be (Some("n^2log(n)", true))
  }

  "Calculating approximate expressions" should "give a correct upper bound" in {

    val rec0 = "S(n)=S(n/2)+S(n/3)+O(n)"
    val rec1 = "S(n)=3S(n/3)+6S(n/4)+O(n^2)"
    RecurrenceCalculator.complexity(rec0) should be (Some("nlog(n)", false))
    RecurrenceCalculator.complexity(rec1) should be (Some("n^2log(n)", false))
  }
}
