package com.peknight.generic.tuple.ops

import cats.{Applicative, Eval, Functor, Id, Semigroupal}
import com.peknight.generic.tuple.{Map, Reverse, ZipWithIndex}

import scala.Tuple.{Last, Zip}
import scala.annotation.tailrec

object TupleOps:

  @tailrec private[ops] def loop[F[_]](remain: Tuple, acc: F[Tuple])(f: [A] => (A, F[Tuple]) => F[Tuple])
  : F[Tuple] =
    remain match
      case _: EmptyTuple => acc
      case h *: t => loop(t, f(h, acc))(f)
  end loop

  def isEmpty[T <: Tuple](tuple: T): Boolean =
    tuple match
      case _: EmptyTuple => true
      case _ => false
  end isEmpty

  def nonEmpty[T <: Tuple](tuple: T): Boolean = !isEmpty(tuple)

  def _reverse[T <: Tuple](tuple: T): Reverse[T] =
    loop[Id](tuple, EmptyTuple)([A] => (a: A, acc: Id[Tuple]) => a *: acc).asInstanceOf[Reverse[T]]

  def flatMap[T <: Tuple, F[_] <: Tuple](tuple: T)(f: [A] => A => F[A]): Tuple.FlatMap[T, F] =
    loop[Id](_reverse(tuple), EmptyTuple)([A] => (a: A, acc: Id[Tuple]) => f(a) ++ acc)
      .asInstanceOf[Tuple.FlatMap[T, F]]

  def traverse[T <: Tuple, F[_], G[_] : Applicative](tuple: T)(f: [A] => A => G[F[A]]): G[Map[T, F]] =
    loop[G](_reverse(tuple), Applicative[G].pure(EmptyTuple))(
      [A] => (a: A, acc: G[Tuple]) => Applicative[G].map2(f(a), acc)(_ *: _)
    ).asInstanceOf[G[Map[T, F]]]

  @tailrec def foldLeft[B](tuple: Tuple, b: B)(f: [A] => (B, A) => B): B =
    tuple match
      case _: EmptyTuple => b
      case h *: t => foldLeft(t, f(b, h))(f)

  def foldRight[B](tuple: Tuple, lb: Eval[B])(f: [A] => (A, Eval[B]) => Eval[B]): Eval[B] =
    def loop(elems: Tuple): Eval[B] =
      elems match
        case _: EmptyTuple => lb
        case h *: t => f(h, Eval.defer(loop(t)))
    end loop
    Eval.defer(loop(tuple))

  def foldRight[B](tuple: Tuple, b: B)(f: [A] => (A, B) => B): B =
    foldLeft(_reverse(tuple), b)([A] => (b: B, a: A) => f(a, b))

  @tailrec def foreach[F[_]](tuple: Tuple)(f: [A] => A => F[A]): Unit = tuple match
    case h *: t =>
      f(h)
      foreach(t)(f)
    case _: EmptyTuple => ()

  def forall(tuple: Tuple)(f: [A] => A => Boolean): Boolean =
    @tailrec def loop(t: Tuple, flag: Boolean): Boolean = (t, flag) match
      case (_, false) => false
      case (_: EmptyTuple, _) => true
      case (h *: t, _) => loop(t, f(h))
    loop(tuple, true)

  def exists(tuple: Tuple)(f: [A] => A => Boolean): Boolean =
    @tailrec def loop(t: Tuple, flag: Boolean): Boolean = (t, flag) match
      case (_, true) => true
      case (_: EmptyTuple, _) => false
      case (h *: t, _) => loop(t, f(h))
    loop(tuple, false)

  def sequence[T <: Tuple, G[_] : Applicative](tuple: Map[T, G]): G[T] =
    type F[A] = A match { case G[x] => x }
    traverse[Map[T, G], F, G](tuple)([A] => (a: A) => a.asInstanceOf[G[F[A]]]).asInstanceOf[G[T]]

  def mapN[T <: Tuple, G[_] : Applicative, Z](tuple: Map[T, G])(f: T => Z): G[Z] =
    Applicative[G].map(sequence(tuple))(f)

  def zipWithIndex[T <: Tuple](tuple: T): ZipWithIndex[T] =
    _reverse(foldLeft[(Tuple, Int)](tuple, (EmptyTuple, 0)) { [A] => (acc: (Tuple, Int), a: A) =>
      ((a, acc._2) *: acc._1, acc._2 + 1)
    }._1).asInstanceOf[ZipWithIndex[T]]

  def unzip[T <: Tuple, U <: Tuple](tuple: Zip[T, U]): (T, U) =
    foldRight[(Tuple, Tuple)](tuple, (EmptyTuple, EmptyTuple)){ [A] => (a: A, b: (Tuple, Tuple)) =>
      val ((th, uh), (tt, ut)) = (a, b): @unchecked
      (th *: tt, uh *: ut)
    }.asInstanceOf[(T, U)]

  def mkString[T <: Tuple](tuple: T, start: String, sep: String, end: String): String =
    val builder = new StringBuilder()
    if start.nonEmpty then builder.append(start)
    mkString(tuple, sep, builder)
    if end.nonEmpty then builder.append(end)
    builder.toString()

  def mkString[T <: Tuple](tuple: T, sep: String): String =
    mkString(tuple, sep, new StringBuilder()).toString()

  def mkString[T <: Tuple](tuple: T): String =
    mkString(tuple, "", new StringBuilder()).toString()

  @tailrec private def mkString[T <: Tuple](tuple: T, sep: String, builder: StringBuilder): StringBuilder =
    tuple match
      case _: EmptyTuple => builder
      case h *: (_: EmptyTuple) => builder.append(h)
      case h *: t => mkString(t, sep, builder.append(h).append(sep))

end TupleOps
