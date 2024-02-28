package com.peknight.generic.migration.instances

import cats.syntax.applicative.*
import cats.{Applicative, Monoid}
import com.peknight.generic.Mirror
import com.peknight.generic.migration.Migration
import com.peknight.generic.tuple.Second
import com.peknight.generic.tuple.ops.{Align, Diff, Intersection, Prepend}

import scala.Tuple.Zip
import scala.compiletime.constValueTuple

trait MigrationInstances:
  //noinspection ConvertExpressionToSAM
  inline given[F[_], A <: Product, ALabels <: Tuple, ARepr <: Tuple, B, BLabels <: Tuple, BRepr <: Tuple,
    Common <: Tuple, Added <: Tuple, Unaligned <: Tuple](
    using
    applicative: Applicative[F],
    aMirror: Mirror.Product.Labelled[A, ALabels, ARepr],
    bMirror: Mirror.Product.Labelled[B, BLabels, BRepr],
    inter: Intersection.Aux[Zip[ALabels, ARepr], Zip[BLabels, BRepr], Common],
    // 定义了diff但是在代码中并没有使用，这里的Diff仅用来计算出Added的实际类型
    diff: Diff.Aux[Zip[BLabels, BRepr], Common, Added],
    // 通过Monoid获取Added
    monoid: Monoid[Added],
    prepend: Prepend.Aux[Added, Common, Unaligned],
    align: Align[Unaligned, Zip[BLabels, BRepr]]
  ): Migration[F, A, B] =
    new Migration[F, A, B]:
      def migrate(a: A): F[B] =
        bMirror.fromProduct(align(prepend(monoid.empty, inter(constValueTuple[ALabels].zip(Tuple.fromProductTyped(a)))))
          .map[Second] {
            [T] => (t: T) => t match
              case (_, value) => value.asInstanceOf[Second[T]]
          }
        ).pure[F]
  end given
end MigrationInstances
object MigrationInstances extends MigrationInstances
