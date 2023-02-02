package com.peknight.generic.deriving

import scala.deriving.Mirror as SMirror

object Mirror:
  type Aux[A, Repr <: Tuple] = SMirror.Of[A] {type MirroredElemTypes = Repr}

  type Labelled[A, Labels <: Tuple, Repr <: Tuple] = SMirror.Of[A] {
    type MirroredElemLabels = Labels
    type MirroredElemTypes = Repr
  }

  object Product:
    type Aux[A, Repr <: Tuple] = SMirror.ProductOf[A] {type MirroredElemTypes = Repr}
    type Labelled[A, Labels <: Tuple, Repr <: Tuple] = SMirror.ProductOf[A] {
      type MirroredElemLabels = Labels
      type MirroredElemTypes = Repr
    }
  end Product

  object Sum:
    type Aux[A, Repr <: Tuple] = SMirror.SumOf[A] {type MirroredElemTypes = Repr}
    type Labelled[A, Labels <: Tuple, Repr <: Tuple] = SMirror.SumOf[A] {
      type MirroredElemLabels = Labels
      type MirroredElemTypes = Repr
    }
  end Sum
end Mirror
