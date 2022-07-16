package ch.epfl.alcmp.scripting

import scala.io.Source

object DCAssembler {

  private val TEMPLATE_PATH = "/dc_template.py"
  private val BASE_CASE_FRAGMENT_PATH = "/dc_base.py"
  private val DIVIDE_FRAGMENT_PATH ="/dc_divide.py"
  private val COMBINE_FRAGMENT_PATH = "/dc_combine.py"

  def readTemplate: String = readContent(TEMPLATE_PATH)

  def readContent(path: String): String =
    val stream = getClass.getResourceAsStream(path)
    if stream == null then null else Source.fromInputStream(stream).mkString

  def insertCode(baseFunction: String, code: String): String =
    s"""$baseFunction
       |    $code""".stripMargin

  private def mkFunction(path: String, code: String): String = insertCode(readContent(path), code)

  def assembleUserFunctions(base: String, div: String, comb: String): String =

    val baseFunc = mkFunction(BASE_CASE_FRAGMENT_PATH, base)
    val divideFunc = mkFunction(DIVIDE_FRAGMENT_PATH, div)
    val combineFunc = mkFunction(COMBINE_FRAGMENT_PATH, comb)
    List(baseFunc, divideFunc, combineFunc).mkString("\n")

  def assemble(base: String, div: String, comb: String): String =

    val userFunctions = assembleUserFunctions(base, div, comb)
    val template = readTemplate

    s"""$userFunctions
       |
       |# Template of D&C Algorithm
       |
       |$template
       |""".stripMargin
}
