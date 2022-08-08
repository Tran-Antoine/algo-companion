package ch.epfl.alcmp.parsing

object RecurrenceCalculator {

  def complexity(recurrence: String): Option[(String, Boolean)] =

    def calcComplexity(value: Equation): Option[(String, Boolean)] =
      val name = value.left.name
      val variable = value.left.variable

      val wrongVariable = !value.right.forall {
        case ComplexityExpression(inner) => inner.variable == variable || inner.variable == '0'
        case FunctionCall(n, v, _, _) => n == name && v == variable
      }

      if wrongVariable then None
      else
        val combineCost = simplifyComplexity(value.right)
        evaluateRecursionCost(value.right) match {
          case None => Some(applyMasterTheorem(combineCost, 0, 0, variable), true)
          case Some(count, fraction, exact) => Some(applyMasterTheorem(combineCost, count, fraction, variable), exact)
        }

    def simplifyComplexity(list: List[OuterTerm]): ComplexityExpression =
      var currentHighest = Polynomial('0', 0)
      for (element <- list) {
        element match {
          case ComplexityExpression(p) =>
            if (p.maxDegree > currentHighest.maxDegree) {
              currentHighest = p
            }
          case _ => ()
        }
      }
      ComplexityExpression(currentHighest)

    def evaluateRecursionCost(list: List[OuterTerm]): Option[(Double, Double, Boolean)] =
      val relevantList: List[FunctionCall] = list.foldLeft[List[FunctionCall]](Nil)((list, b) => b match {
        case f:FunctionCall => f :: list
        case _ => list
      })

      if relevantList.isEmpty then None
      else
        var exact = true
        var count = relevantList.head.outerFactor
        var factor = relevantList.head.innerFactor

        for (call <- relevantList.tail) {
          count += call.outerFactor
          if (call.innerFactor != factor) {
            factor = call.innerFactor.min(factor)
            exact = false
          }
        }
        Some(count, factor, exact)

    def applyMasterTheorem(expression: ComplexityExpression, count: Double, fraction: Double, variable: Char): String =

      val degree = expression.inner.maxDegree

      if count == 0 then if degree != 1 then s"$variable^$degree" else s"$variable"
      else
        val divider = 1/fraction
        val logbA = Math.log(count) / Math.log(divider)

        if degree < logbA      then if logbA  != 1 then s"$variable^$logbA"  else "n"
        else if degree > logbA then if degree != 1 then s"$variable^$degree" else "n"
        else if degree != 1    then s"$variable^${degree}log($variable)"     else s"${variable}log($variable)"


    val parser = new RecurrenceParser()
    parser.parseAll[Equation](parser.equation, recurrence) match {
      case parser.Success(result, _) => calcComplexity(result)
      case _ => None
    }
}
