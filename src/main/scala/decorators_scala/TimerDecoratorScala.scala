package decorators_scala

package decorators

import actions.Action

class TimerDecoratorScala[T, R](action: Action[T, R]) extends Action[T, R]{

  override def run(arg: T): R = {
    val startTime = System.currentTimeMillis
    val result = action.run(arg)
    val endTime = System.currentTimeMillis
    val elapsedTime = endTime - startTime

    System.out.println("Tiempo de ejecución para '" + action.getName + "': " + elapsedTime + " milisegundos")

    result
  }

  /**
   * Obtener el nombre de la acción
   *
   * @return Devuelve un string con el nombre de la acción
   */
  override def getName: String = action.getName
}
