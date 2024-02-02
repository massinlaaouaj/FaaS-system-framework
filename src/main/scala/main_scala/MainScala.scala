package main_scala

import actions_scala.TextToASCIIAction
import model.Controller
import policies.BigGroup

object Main {

  val controller: Controller = new Controller(3, 1024, new BigGroup(2))

  def main(args: Array[String]): Unit = {

    val text: String = "Hola mundo plano"
    val texttoascii = new TextToASCIIAction()

    controller.registerAction("texttoascii", texttoascii, 100)

    controller.invokeAction("texttoascii", text)


  }


  println("Hola")

}
