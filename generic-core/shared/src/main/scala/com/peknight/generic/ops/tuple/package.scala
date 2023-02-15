package com.peknight.generic.ops

import cats.{Applicative, Eval, Id}

import scala.annotation.tailrec

package object tuple:

  type SecondElem[A] = A match { case _ *: s *: _ => s }

  type LiftedTuple[F[_], T <: Tuple] <: Tuple =
    T match
      case h *: t => F[h] *: LiftedTuple[F, t]
      case _ => EmptyTuple

  type TypedTuple[T <: Tuple, A] = T match
    case A *: t => TypedTuple[t, A]
    case EmptyTuple => DummyImplicit

  type Reverse[T <: Tuple] <: Tuple =
    T match
      case h *: t => Tuple.Append[Reverse[t], h]
      case _ => EmptyTuple

  @tailrec private[this] def loop[F[_], T <: Tuple](remain: T, acc: F[Tuple])(f: [A] => (A, F[Tuple]) => F[Tuple])
  : F[Tuple] =
    remain match
      case _: EmptyTuple => acc
      case h *: t => loop(t, f(h, acc))(f)
  end loop

  def reverse[T <: Tuple](tuple: T): Reverse[T] =
    loop[Id, T](tuple, EmptyTuple)([A] => (a: A, acc: Id[Tuple]) => a *: acc).asInstanceOf[Reverse[T]]

  def flatMap[F[_] <: Tuple, T <: Tuple](tuple: T)(f: [A] => A => F[A]): Tuple.FlatMap[T, F] =
    loop[Id, Reverse[T]](reverse(tuple), EmptyTuple) {
      [A] => (a: A, acc: Id[Tuple]) =>
        f(a) ++ acc
    }.asInstanceOf[Tuple.FlatMap[T, F]]

  def traverse[F[_], G[_] : Applicative, T <: Tuple](fa: T)(f: [A] => A => G[F[A]]): G[LiftedTuple[F, T]] =
    loop[G, Reverse[T]](reverse(fa), Applicative[G].pure(EmptyTuple)) {
      [A] => (a: A, acc: G[Tuple]) =>
        Applicative[G].map2(f(a), acc)(_ *: _)
    }.asInstanceOf[G[LiftedTuple[F, T]]]

  def sequence[G[_] : Applicative, T <: Tuple](fga: LiftedTuple[G, T]): G[T] =
    traverse[Id, G, LiftedTuple[G, T]](fga)([A] => (a: A) => a.asInstanceOf).asInstanceOf[G[T]]

  @tailrec def foldLeft[T <: Tuple, B](fa: T, b: B)(f: [A] => (B, A) => B): B =
    fa match
      case _: EmptyTuple => b
      case h *: t => foldLeft(t, f(b, h))(f)
  end foldLeft

  def foldRight[T <: Tuple, B](fa: T, lb: Eval[B])(f: [A] => (A, Eval[B]) => Eval[B]): Eval[B] =
    def loop[U <: Tuple](elems: U): Eval[B] =
      elems match
        case _: EmptyTuple => lb
        case h *: t => f(h, Eval.defer(loop(t)))
    end loop

    Eval.defer(loop(fa))

  def foldRight[T <: Tuple, B](fa: T, b: B)(f: [A] => (A, B) => B): B =
    foldLeft(reverse(fa), b)([A] => (b: B, a: A) => f(a, b))

  def mapN[G[_] : Applicative, T <: Tuple, U](fga: LiftedTuple[G, T])(f: T => U): G[U] =
    Applicative[G].map(sequence[G, T](fga))(f)

end tuple
