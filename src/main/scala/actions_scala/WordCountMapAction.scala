package actions_scala

import actions.Action

class WordCountMapAction extends Action[String, Map[String, Int]] {

  /**
   * Ejecución de la tarea en la acción/función escoguida.
   *
   * @param arg Parámetro de entrada.
   * @return Valor de retorno.
   * @throws Exception
   */
  def run(text: String): Map[String, Int] = {

    // Convierte el texto en una lista de palabras
    val words = text.split("\\s+").toList

    // Cuenta las ocurrencias de cada palabra y devuelve un Map
    words.map(f => f.toUpperCase())
      .groupBy(identity)
      .view
      .mapValues(_.size)
      .toMap
  }

  /**
   * Obtener el nombre de la acción
   *
   * @return Devuelve un string con el nombre de la acción
   */
  override def getName: String = "wordCountMapActionScala"
}
