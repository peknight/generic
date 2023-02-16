package com.peknight.generic.deriving

import scala.deriving.Mirror as SMirror

object Mirror:

  type Aux[A, Repr <: Tuple] = Mirror[A] { type MirroredElemTypes = Repr }

  type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Mirror[A] {
    type MirroredElemLabels = Labels
    type MirroredElemTypes = Repr
  }

  type Product[A] = SMirror.ProductOf[A]

  object Product:
    type Aux[A, Repr <: Tuple] = Product[A] { type MirroredElemTypes = Repr }
    type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Product[A] {
      type MirroredElemLabels = Labels
      type MirroredElemTypes = Repr
    }
  end Product

  type Sum[A] = SMirror.SumOf[A]

  object Sum:
    type Aux[A, Repr <: Tuple] = Sum[A] { type MirroredElemTypes = Repr }
    type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Sum[A] {
      type MirroredElemLabels = Labels
      type MirroredElemTypes = Repr
    }
  end Sum
end Mirror
