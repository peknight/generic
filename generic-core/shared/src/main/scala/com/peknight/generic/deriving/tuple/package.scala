package com.peknight.generic.deriving

import scala.compiletime.{constValue, erasedValue, summonInline}

package object tuple:

  //noinspection DuplicatedCode
  inline def summonAsTuple[A <: Tuple]: A = inline erasedValue[A] match
    case _: EmptyTuple => EmptyTuple.asInstanceOf[A]
    case _: (h *: t) => (summonInline[h] *: summonAsTuple[t]).asInstanceOf[A]

  //noinspection DuplicatedCode
  inline def summonValuesAsTuple[A <: Tuple]: A = inline erasedValue[A] match
    case _: EmptyTuple => EmptyTuple.asInstanceOf[A]
    case _: (h *: t) => (constValue[h] *: summonValuesAsTuple[t]).asInstanceOf[A]

end tuple