package com.peknight.generic

import cats.{Applicative, Eval, Id, Traverse}

import scala.annotation.tailrec
import scala.compiletime.{constValue, erasedValue, summonInline}

package object tuple:

  //noinspection DuplicatedCode
  inline def summonAsTuple[A <: Tuple]: A = inline erasedValue[A] match
    case _: EmptyTuple => EmptyTuple.asInstanceOf[A]
    case _: (h *: t) => (summonInline[h] *: summonAsTuple[t]).asInstanceOf[A]

  //noinspection DuplicatedCode
  inline def summonValuesAsTuple[A <: Tuple]: A = inline erasedValue[A] match
    case _: EmptyTuple => EmptyTuple.asInstanceOf[A]
    case _: (h *: t) => (constValue[h] *: summonValuesAsTuple[t]).asInstanceOf[A]

  type Second[A] = A match { case _ *: s *: _ => s }

  type LiftP[F[_], T] <: Tuple =
    T match
      case h *: t => F[h] *: LiftP[F, t]
      case _ => EmptyTuple

  type Reverse[T <: Tuple] <: Tuple =
    T match
      case h *: t => Tuple.Append[Reverse[t], h]
      case _ => EmptyTuple

  def reverse[T <: Tuple](tuple: T): Reverse[T] =
    @tailrec def loop[U <: Tuple, V <: Tuple](remain: U, acc: V): Tuple =
      remain match
        case _: EmptyTuple => acc
        case h *: t => loop(t, h *: acc)
    loop(tuple, EmptyTuple).asInstanceOf[Reverse[T]]

  def traverse[F[_], G[_] : Applicative, T <: Tuple](fa: T)(f: [A] => A => G[F[A]]): G[LiftP[F, T]] =
    val rfa: Reverse[T] = reverse(fa)
    @tailrec def loop[U <: Tuple, V <: Tuple](remain: U, acc: G[V]): G[Tuple] =
      remain match
        case _: EmptyTuple => acc.asInstanceOf[G[Tuple]]
        case h *: t => loop(t, Applicative[G].map2(f(h), acc)(_ *: _))
    loop(rfa, Applicative[G].pure[Tuple](EmptyTuple)).asInstanceOf[G[LiftP[F, T]]]
  end traverse

  def sequence[G[_] : Applicative, T <: Tuple](fga: LiftP[G, T]): G[T] =
    traverse[Id, G, LiftP[G, T]](fga)([A] => (a: A) => a.asInstanceOf).asInstanceOf[G[T]]

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

end tuple