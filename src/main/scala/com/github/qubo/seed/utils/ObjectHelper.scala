package com.github.qubo.seed.utils

object ObjectHelper {
  implicit class RichSeq[+A](seq: Seq[A]) {
    def tap(function: A => Unit): Seq[A] =
      seq.map { item =>
        function(item)
        item
      }
  }
}
