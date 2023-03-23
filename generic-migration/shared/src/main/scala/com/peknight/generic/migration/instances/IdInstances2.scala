package com.peknight.generic.migration.instances

import cats.{Id, Monoid}
import com.peknight.generic.deriving.Mirror
import com.peknight.generic.migration.Migration
import com.peknight.generic.tuple.*
import com.peknight.generic.tuple.ops.*

import scala.compiletime.{constValue, constValueTuple}

trait IdInstances2:

  given [A]: Migration[A, A] with
    def run(a: A): Id[A] = a
  end given

  given [A <: Product, Repr <: Tuple] (using mirror: Mirror.Product.Aux[A, Repr]): Migration[A, Repr] with
    def run(a: A): Id[Repr] = Tuple.fromProductTyped(a)
  end given

  given [A, Repr <: Tuple] (using mirror: Mirror.Product.Aux[A, Repr]): Migration[Repr, A] with
    def run(a: Repr): Id[A] = mirror.fromProduct(a)
  end given

  inline given[A <: Product, ALabels <: Tuple, ARepr <: Tuple, B, BLabels <: Tuple, BRepr <: Tuple,
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
  ): Migration[A, B] = (a: A) =>
    bMirror.fromProduct(align(prepend(monoid.empty, inter(constValueTuple[ALabels].zip(Tuple.fromProductTyped(a)))))
      .map[Second] {
        [T] => (t: T) => t match
          case (_, value) => value.asInstanceOf[Second[T]]
      }
    )
  end given

end IdInstances2
