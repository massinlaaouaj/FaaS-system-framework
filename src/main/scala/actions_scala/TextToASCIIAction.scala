package actions_scala

import actions.Action

class TextToASCIIAction extends Action[String, String]{
  /**
   * Ejecución de la tarea en la acción/función escoguida.
   *
   * @param arg Parámetro de entrada.
   * @return Valor de retorno.
   * @throws Exception
   */
  override def run(text: String): String = {
    val resultado = new StringBuilder

    for (i <- 0 until text.length) {
      val caracter = text.charAt(i)
      val codigoAscii = caracter.toInt
      resultado.append(s"$codigoAscii ")
    }

    println("Text in ASCII code: " + resultado.toString().trim)

    resultado.toString().trim
  }

  /**
   * Obtener el nombre de la acción
   *
   * @return Devuelve un string con el nombre de la acción
   */
  override def getName: String = "textToASCIIAction"
}
