package ch.epfl.alcmp.scripting

import scala.io.Source

object DCAssembler {

  private val TEMPLATE_PATH = "/dc_template.py"

  def readTemplate: String = readTemplate(TEMPLATE_PATH)

  def readTemplate(path: String): String =
    val stream = getClass.getResourceAsStream(path)
    if stream == null then null else Source.fromInputStream(stream).mkString

  def assemble(base: String, div: String, comb: String): String = ???

}
