package decorators_scala

package decorators

import actions.Action

import java.util.Map
import java.util.concurrent.locks.{Lock, ReentrantLock}

class MemoizaitionDecoratorScala[T, R](action: Action[T, R], cache: Map[T, R], lock: Lock = new ReentrantLock()) extends Action[T, R]{

  /**
   * Todo lo que sea una acción tendrá que tener el método run.
   *
   * @param arg Parámetro de tipo T que es el argumento de la accion
   * @return Valor de tipo R.
   */
  override def run(arg: T): R = {
    // Si estamos trabajando con caché, bloqueamos el  hilo, pues la caché
    // es una sección crítica del código.
    this.lock.lock()

    // Observamos si esta contenido en la caché.// Observamos si esta contenido en la caché.
    if (this.cache.containsKey(arg)) {
      // Desbloqueamos el hilo, pues cumple la condición de encontrar en la caché.
      this.lock.unlock()
      System.out.println("Recuperando resultado de caché para '" + action.getName + "' con paràmetro " + arg)
      // Devolvemos desde la caché.
      return this.cache.get(arg)
    }
    else { // En caso de no encontrarse en la caché, la almacenamos.
      val result = action.run(arg)
      this.cache.put(arg, result)
      this.lock.unlock() // Desbloqueamos después de añadirlo en caché, ya que sinó se corrompe si diferentes actuan al mismo tiempo.

      return result
    }
  }

  /**
   * Obtener el nombre de la acción
   *
   * @return Devuelve un string con el nombre de la acción
   */
  override def getName: String = action.getName
}

