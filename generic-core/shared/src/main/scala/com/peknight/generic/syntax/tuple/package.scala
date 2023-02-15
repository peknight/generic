package com.peknight.generic.syntax

import cats.{Applicative, Eval}
import com.peknight.generic.ops
import com.peknight.generic.ops.tuple.{LiftedTuple, Reverse}

package object tuple:
  extension [T <: Tuple] (tuple: T)
    def reverse: Reverse[T] = ops.tuple.reverse(tuple)
    def flatMap[F[_] <: Tuple](f: [A] => A => F[A]): Tuple.FlatMap[T, F] = ops.tuple.flatMap(tuple)(f)
    def traverse[F[_], G[_] : Applicative](f: [A] => A => G[F[A]]): G[LiftedTuple[F, T]] = ops.tuple.traverse(tuple)(f)
    def foldLeft[B](b: B)(f: [A] => (B, A) => B): B = ops.tuple.foldLeft(tuple, b)(f)
    def foldRight[B](lb: Eval[B])(f: [A] => (A, Eval[B]) => Eval[B]): Eval[B] = ops.tuple.foldRight(tuple, lb)(f)
    def foldRight[B](b: B)(f: [A] => (A, B) => B): B = ops.tuple.foldRight(tuple, b)(f)
  end extension

  extension [G[_] : Applicative, T <: Tuple] (tuple: LiftedTuple[G, T])
    def sequence: G[T] = ops.tuple.sequence(tuple)
    def mapN[U](f: T => U): G[U] = ops.tuple.mapN(tuple)(f)
  end extension
end tuple
