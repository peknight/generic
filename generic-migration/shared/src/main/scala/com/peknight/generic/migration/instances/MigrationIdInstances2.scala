package com.peknight.generic.migration.instances

import cats.{Id, Monoid}
import com.peknight.generic.Mirror
import com.peknight.generic.migration.id.Migration
import com.peknight.generic.tuple.Second
import com.peknight.generic.tuple.ops.{Align, Diff, Intersection, Prepend}

import scala.Tuple.Zip
import scala.compiletime.constValueTuple

trait MigrationIdInstances2:
  given [A]: Migration[A, A] with
    def migrate(a: A): Id[A] = a
  end given

  given [A <: Product, Repr <: Tuple] (using mirror: Mirror.Product.Aux[A, Repr]): Migration[A, Repr] with
    def migrate(a: A): Id[Repr] = Tuple.fromProductTyped(a)
  end given

  given [A, Repr <: Tuple] (using mirror: Mirror.Product.Aux[A, Repr]): Migration[Repr, A] with
    def migrate(a: Repr): Id[A] = mirror.fromProduct(a)
  end given

  //noinspection ConvertExpressionToSAM
  inline given [A <: Product, ALabels <: Tuple, ARepr <: Tuple, B, BLabels <: Tuple, BRepr <: Tuple,
    Common <: Tuple, Added <: Tuple, Unaligned <: Tuple](
    using
    aMirror: Mirror.Product.Labelled[A, ALabels, ARepr],
    bMirror: Mirror.Product.Labelled[B, BLabels, BRepr],
    inter: Intersection.Aux[Zip[ALabels, ARepr], Zip[BLabels, BRepr], Common],
    // 定义了diff但是在代码中并没有使用，这里的Diff仅用来计算出Added的实际类型
    diff: Diff.Aux[Zip[BLabels, BRepr], Common, Added],
    // 通过Monoid获取Added
    monoid: Monoid[Added],
    prepend: Prepend.Aux[Added, Common, Unaligned],
    align: Align[Unaligned, Zip[BLabels, BRepr]]
  ): Migration[A, B] =
    new Migration[A, B]:
      def migrate(a: A): B =
        bMirror.fromProduct(align(prepend(monoid.empty, inter(constValueTuple[ALabels].zip(Tuple.fromProductTyped(a)))))
          .map[Second] {
            [T] => (t: T) => t match
              case (_, value) => value.asInstanceOf[Second[T]]
          }
        )
  end given
end MigrationIdInstances2
