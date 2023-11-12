package com.peknight.generic.migration.instances

import com.peknight.generic.Mirror
import com.peknight.generic.migration.id.Migration
import com.peknight.generic.tuple.Second
import com.peknight.generic.tuple.ops.{Align, Intersection}

import scala.Tuple.Zip
import scala.compiletime.{constValue, constValueTuple}

trait MigrationIdInstances extends MigrationIdInstances2:
  //noinspection ConvertExpressionToSAM
  inline given[A <: Product, ALabels <: Tuple, ARepr <: Tuple, B, BLabels <: Tuple, BRepr <: Tuple,
    Common <: Tuple, Unaligned <: Tuple](
    using
    aMirror: Mirror.Product.Labelled[A, ALabels, ARepr],
    bMirror: Mirror.Product.Labelled[B, BLabels, BRepr],
    inter: Intersection.Aux[Zip[ALabels, ARepr], Zip[BLabels, BRepr], Common],
    align: Align[Common, Zip[BLabels, BRepr]]
  ): Migration[A, B] =
    new Migration[A, B]:
      def migrate(a: A): B =
        bMirror.fromProduct(align(inter(constValueTuple[ALabels].zip(Tuple.fromProductTyped(a))))
          .map[Second] {
            [T] => (t: T) => t match
              case (_, value) => value.asInstanceOf[Second[T]]
          }
        )
  end given
end MigrationIdInstances
object MigrationIdInstances extends MigrationIdInstances
