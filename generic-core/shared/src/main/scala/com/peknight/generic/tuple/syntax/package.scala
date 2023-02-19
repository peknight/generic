package com.peknight.generic.tuple

import cats.{Applicative, Eval, Id}
import com.peknight.generic.tuple.{Lifted, Reverse}

import scala.annotation.tailrec
package object syntax:
  @tailrec private[this] def loop[F[_], T <: Tuple](remain: T, acc: F[Tuple])(f: [A] => (A, F[Tuple]) => F[Tuple])
  : F[Tuple] =
    remain match
      case _: EmptyTuple => acc
      case h *: t => loop(t, f(h, acc))(f)
  end loop

  extension[T <: Tuple] (tuple: T)
    def reverse: Reverse[T] =
      loop[Id, T](tuple, EmptyTuple)([A] => (a: A, acc: Id[Tuple]) => a *: acc).asInstanceOf[Reverse[T]]

    def flatMap[F[_] <: Tuple](f: [A] => A => F[A]): Tuple.FlatMap[T, F] =
      loop[Id, Reverse[T]] (tuple.reverse, EmptyTuple)([A] => (a: A, acc: Id[Tuple]) => f(a) ++ acc)
        .asInstanceOf[Tuple.FlatMap[T, F]]

    def traverse[F[_], G[_] : Applicative](f: [A] => A => G[F[A]]): G[Lifted[F, T]] =
      loop[G, Reverse[T]](tuple.reverse, Applicative[G].pure(EmptyTuple))(
        [A] => (a: A, acc: G[Tuple]) => Applicative[G].map2(f(a), acc)(_ *: _)
      ).asInstanceOf[G[Lifted[F, T]]]

    @tailrec def foldLeft[B](b: B)(f: [A] => (B, A) => B): B = tuple match
      case _: EmptyTuple => b
      case h *: t => t.foldLeft(f(b, h))(f)

    def foldRight[B](lb: Eval[B])(f: [A] => (A, Eval[B]) => Eval[B]): Eval[B] =
      def loop[U <: Tuple](elems: U): Eval[B] =
        elems match
          case _: EmptyTuple => lb
          case h *: t => f(h, Eval.defer(loop(t)))
      end loop
      Eval.defer(loop(tuple))

    def foldRight[B](b: B)(f: [A] => (A, B) => B): B = tuple.reverse.foldLeft(b)([A] => (b: B, a: A) => f(a, b))

    @tailrec def foreach[F[_]](f: [A] => A => F[A]): Unit = tuple match
      case h *: t =>
        f(h)
        t.foreach(f)
      case _: EmptyTuple => ()

    def forall(f: [A] => A => Boolean): Boolean =
      @tailrec def loop(t: Tuple, flag: Boolean): Boolean = (t, flag) match
        case (_, false) => false
        case (_: EmptyTuple, _) => true
        case (h *: t, _) => loop(t, f(h))
      loop(tuple, true)

    def exists(f: [A] => A => Boolean): Boolean =
      @tailrec def loop(t: Tuple, flag: Boolean): Boolean = (t, flag) match
        case (_, true) => true
        case (_: EmptyTuple, _) => false
        case (h *: t, _) => loop(t, f(h))
      loop(tuple, false)

  end extension

  extension[G[_] : Applicative, T <: Tuple] (tuple: Lifted[G, T])
    def sequence: G[T] = tuple.traverse[Id, G]([A] => (a: A) => a.asInstanceOf).asInstanceOf[G[T]]

    def mapN[U](f: T => U): G[U] = Applicative[G].map(tuple.sequence)(f)
  end extension

end syntax
