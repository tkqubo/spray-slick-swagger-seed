package com.github.qubo.seed.persistence

import org.specs2.mutable.Specification
import org.specs2.specification.process.RandomSequentialExecution

import scala.reflect.runtime.universe._

trait SpecBase
  extends Specification
  with RandomSequentialExecution {

  def target[T: TypeTag]: String = typeOf[T].typeSymbol.fullName
}
