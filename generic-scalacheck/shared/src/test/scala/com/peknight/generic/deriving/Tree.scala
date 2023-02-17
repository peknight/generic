package com.peknight.generic.deriving

import cats.Monad
import org.scalacheck.Gen

import scala.annotation.tailrec

enum Tree[T]:
  case Branch(left: Tree[T], right: Tree[T])
  case Leaf(elem: T)

  def to: List[Option[T]] =
    @tailrec def loop(open: List[Tree[T]], closed: List[Option[T]]): List[Option[T]] = open match
      case Branch(left, right) :: tail => loop(left :: right :: tail, None :: closed)
      case Leaf(elem) :: tail => loop(tail, Some(elem) :: closed)
      case Nil => closed
    loop(List(this), Nil)

  def toList: List[T] = to.foldLeft(List.empty[T])((l: List[T], o: Option[T]) => o.map(_ :: l).getOrElse(l))

end Tree

object Tree:
  def branch[T](left: Tree[T], right: Tree[T]): Tree[T] = Branch(left, right)
  def leaf[T](elem: T): Tree[T] = Leaf(elem)

  def from[T](list: List[Option[T]]): Tree[T] =
    list.foldLeft(List.empty[Tree[T]]) { (acc: List[Tree[T]], maybe: Option[T]) =>
      maybe.map(Leaf(_) :: acc).getOrElse {
        val left :: right :: tail = acc: @unchecked
        Branch(left, right) :: tail
      }
    }.head

  def gen[T](genT: Gen[T], maxSize: Int): Gen[Tree[T]] =
    require(maxSize > 0)
    Gen.choose(1, maxSize).flatMap(size => Gen.tailRecM[(List[Int], List[Option[T]]), Tree[T]]((List(size), Nil)) {
      case (Nil, acc) => Gen.const(Right(from(acc)))
      case (1 :: restSize, acc) => genT.map(elem => Left((restSize, Some(elem) :: acc)))
      case (sizeHead :: sizeTail, acc) =>  Gen.choose(1, sizeHead - 1).map(leftSize => Left((
        leftSize :: sizeHead - leftSize :: sizeTail,
        None :: acc
      )))
    })

  given Monad[Tree] with
    def pure[A](x: A): Tree[A] = Leaf(x)
    def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = from(fa.to.flatMap {
      case Some(a) => f(a).to
      case None => List(None)
    })
    def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
      @tailrec def loop(open: List[Tree[Either[A, B]]], closed: List[Option[Tree[B]]]): List[Tree[B]] = open match
        case Branch(l, r) :: next => loop(l :: r :: next, None :: closed)
        case Leaf(Left(value)) :: next => loop(f(value) :: next, closed)
        case Leaf(Right(value)) :: next => loop(next, Some(pure(value)) :: closed)
        case Nil => closed.foldLeft(Nil: List[Tree[B]]) { (acc: List[Tree[B]], maybeTree: Option[Tree[B]]) =>
          maybeTree.map(_ :: acc).getOrElse {
            val left :: right :: tail = acc: @unchecked
            Tree.branch(left, right) :: tail
          }
        }
      loop(List(f(a)), Nil).head
  end given
end Tree

