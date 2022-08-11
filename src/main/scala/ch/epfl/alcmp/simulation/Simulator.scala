package ch.epfl.alcmp.simulation

import ch.epfl.alcmp.scripting.DCAssembler

import java.io.{File, FileWriter}

class Simulator {

  private val BIN_FOLDER = "src/main/bin/"

  def makeDCScript(base: String, div: String, comb: String): Unit =
    val script = DCAssembler.assemble(base, div, comb)
    val writer: FileWriter = new FileWriter(new File(BIN_FOLDER + "DC_script.py"))
    writer.write(script)
    writer.close()



}
