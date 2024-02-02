package actions_scala

import actions.Action

class CountWordsAction extends Action[String, Map[String, Int]] {

  /**
   * Ejecución de la tarea en la acción/función escoguida.
   *
   * @param arg Parámetro de entrada.
   * @return Valor de retorno.
   * @throws Exception
   */
  override def run(text: String): Map[String, Int] = {
    // Convierte el texto en una lista de palabras
    val words: List[String] = text.split("\\s+").toList
    val numberWords: Int = words.distinct.size

    words.map(f => f.toUpperCase())
      .distinct
      .groupBy(identity)
      .view
      .mapValues(_ => numberWords)
      .toMap
  }

  /**
   * Obtener el nombre de la acción
   *
   * @return Devuelve un string con el nombre de la acción
   */
  override def getName: String = "countWordsActionScala"


}
