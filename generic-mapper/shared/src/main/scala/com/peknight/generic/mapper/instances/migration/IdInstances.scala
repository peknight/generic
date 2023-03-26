package com.peknight.generic.mapper.instances.migration

import com.peknight.generic.deriving.Mirror
import com.peknight.generic.mapper.Migration
import com.peknight.generic.tuple.*
import com.peknight.generic.tuple.ops.*

import scala.compiletime.{constValue, constValueTuple}

trait IdInstances extends IdInstances2:

  inline given [A <: Product, ALabels <: Tuple, ARepr <: Tuple, B, BLabels <: Tuple, BRepr <: Tuple,
    Common <: Tuple, Unaligned <: Tuple](
    using
    aMirror: Mirror.Product.Labelled[A, ALabels, ARepr],
    bMirror: Mirror.Product.Labelled[B, BLabels, BRepr],
    inter: Intersection.Aux[Tuple.Zip[ALabels, ARepr], Tuple.Zip[BLabels, BRepr], Common],
    align: Align[Common, Tuple.Zip[BLabels, BRepr]]
  ): Migration[A, B] = (a: A) =>
    bMirror.fromProduct(align(inter(constValueTuple[ALabels].zip(Tuple.fromProductTyped(a))))
      .map[Second] { [T] => (t: T) => t match
        case (_, value) => value.asInstanceOf[Second[T]]
      }
    )
  end given
end IdInstances

object IdInstances extends IdInstances
