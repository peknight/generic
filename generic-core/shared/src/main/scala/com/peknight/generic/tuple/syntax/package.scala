package com.peknight.generic.tuple

import cats.{Applicative, Eval, Functor, Semigroupal}
import com.peknight.generic.tuple.ops.{NonEmptyTupleOps, TupleOps}
import com.peknight.generic.tuple.{Map, Reverse}

import scala.Tuple.Zip

package object syntax:
  extension [T <: Tuple] (tuple: T)
    def reverse: Reverse[T] = TupleOps.reverse(tuple)

    def flatMap[F[_] <: Tuple](f: [A] => A => F[A]): Tuple.FlatMap[T, F] = TupleOps.flatMap[T, F](tuple)(f)

    def traverse[F[_], G[_] : Applicative](f: [A] => A => G[F[A]]): G[Map[T, F]] = 
      TupleOps.traverse[T, F, G](tuple)(f)

    def foldLeft[B](b: B)(f: [A] => (B, A) => B): B = TupleOps.foldLeft[B](tuple, b)(f)

    def foldRight[B](lb: Eval[B])(f: [A] => (A, Eval[B]) => Eval[B]): Eval[B] = TupleOps.foldRight[B](tuple, lb)(f)

    def foldRight[B](b: B)(f: [A] => (A, B) => B): B = TupleOps.foldRight[B](tuple, b)(f)

    def foreach[F[_]](f: [A] => A => F[A]): Unit = TupleOps.foreach[F](tuple)(f)

    def forall(f: [A] => A => Boolean): Boolean = TupleOps.forall(tuple)(f)

    def exists(f: [A] => A => Boolean): Boolean = TupleOps.exists(tuple)(f)

  end extension

  extension [T <: Tuple, G[_] : Applicative] (tuple: Map[T, G])
    def sequence: G[T] = TupleOps.sequence[T, G](tuple)

    def mapN[Z](f: T => Z): G[Z] = TupleOps.mapN[T, G, Z](tuple)(f)
  end extension

  extension [T <: Tuple, U <: Tuple] (tuple: Zip[T, U])
    def unzip: (T, U) = TupleOps.unzip[T, U](tuple)
  end extension

  object nonEmptyTuple:
    extension [T <: NonEmptyTuple, G[_] : Functor : Semigroupal] (tuple: NonEmptyMap[T, G])
      def mapN[Z](f: T => Z): G[Z] = NonEmptyTupleOps.mapN[T, G, Z](tuple)(f)
    end extension
  end nonEmptyTuple
end syntax
