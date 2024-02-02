package model_scala

import actions.Action
import model.{Controller, Invoker}
import observer.Observable
import policies.{GreedyGroup, PolicyManager}

class ControllerScala(numberOfInvoker: Integer = 1, memoryOfInvokers: Integer = 1024, pm: PolicyManager = GreedyGroup) {

  val controller: Controller = new Controller(numberOfInvoker, memoryOfInvokers, pm)

  def registerAction[T, R](actionName: String, action: Action[T, R], memoryNeeded: Integer)
  = controller.registerAction(actionName, action, memoryNeeded)

  def invokeAction[T](actionName: String, input: List[T])
  = controller.invokeAction(actionName, input)

  def invokeAction_async[T](actionName: String, input: List[T])
  = controller.invokeAction_async(actionName, input)

  def invokeAction_async[T](actionName: String, input: List[T])
  = controller.invokeAction_async(actionName, input)

  def createActionProxy(actionName: String, isAsync: Boolean, isGroupalInvoke: Boolean)
  = controller.createActionProxy(actionName, isAsync, isGroupalInvoke)

  //Observable observable, String taskName, long executionTime, int memoryNeeded
  def update(observable: Observable, taskName: String, executionTime: Long, memoryNeeded: Int)
  = controller.update(observable, taskName, executionTime, memoryNeeded)

  def showMetrics()
  = controller.showMetrics()

  def add(arg1: Int, arg2: Int)
  = controller.add(arg1, arg2)

}
