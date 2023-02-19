package com.peknight.generic.migration.instances

import cats.data.Kleisli
import cats.{Id, Monoid}
import com.peknight.generic.compiletime.summonValuesAsTuple
import com.peknight.generic.deriving.Mirror
import com.peknight.generic.migration.Migration
import com.peknight.generic.tuple.*
import com.peknight.generic.tuple.ops.*

import scala.compiletime.constValue

trait IdInstances:
  inline given [A <: Product, Repr <: Tuple](using mirror: Mirror.Product.Aux[A, Repr]): Migration[A, Repr] =
    Kleisli(Tuple.fromProductTyped)

  inline given [A, Repr <: Tuple](using mirror: Mirror.Product.Aux[A, Repr]): Migration[Repr, A] =
    Kleisli(mirror.fromProduct)

  given Monoid[EmptyTuple] = Monoid.instance(EmptyTuple, (_, _) => EmptyTuple)

  inline given [K <: String, H, T <: Tuple](using hMonoid: => Monoid[H], tMonoid: Monoid[T]): Monoid[(K, H) *: T] =
    val label = constValue[K]
    Monoid.instance(
      (label, hMonoid.empty) *: tMonoid.empty,
      (x, y) => (label, hMonoid.combine(x.head._2, y.head._2)) *: tMonoid.combine(x.tail, y.tail)
    )
  end given

  inline given [A <: Product, ALabels <: Tuple, ARepr <: Tuple, B, BLabels <: Tuple, BRepr <: Tuple,
    Common <: Tuple, Added <: Tuple, Unaligned <: Tuple](
    using
    aMirror: Mirror.Product.Labelled[A, ALabels, ARepr],
    bMirror: Mirror.Product.Labelled[B, BLabels, BRepr],
    inter: Intersection.Aux[Tuple.Zip[ALabels, ARepr], Tuple.Zip[BLabels, BRepr], Common],
    // 定义了diff但是在代码中并没有使用，这里的Diff仅用来计算出Added的实际类型
    diff: Diff.Aux[Tuple.Zip[BLabels, BRepr], Common, Added],
    // 通过Monoid获取Added
    monoid: Monoid[Added],
    prepend: Prepend.Aux[Added, Common, Unaligned],
    align: Align[Unaligned, Tuple.Zip[BLabels, BRepr]]
  ): Migration[A, B] = Kleisli((a: A) => bMirror.fromProduct(
    align(prepend(monoid.empty, inter(summonValuesAsTuple[ALabels].zip(Tuple.fromProductTyped(a))))).map[Second] {
      [T] => (t: T) => t match
        case (_, value) => value.asInstanceOf[Second[T]]
    }
  ))
end IdInstances

object IdInstances extends IdInstances
