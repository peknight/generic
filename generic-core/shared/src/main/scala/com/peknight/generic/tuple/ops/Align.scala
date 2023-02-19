package com.peknight.generic.tuple.ops

/**
 * Type class supporting permuting this `HList` into the same order as another `HList` with
 * the same element types.
 *
 * @author Miles Sabin
 */
private[generic] trait Align[L <: Tuple, M <: Tuple] extends (L => M) with Serializable:
  def apply(l: L): M
end Align

private[generic] object Align:
  def apply[L <: Tuple, M <: Tuple](using alm: Align[L, M]): Align[L, M] = alm

  given Align[EmptyTuple, EmptyTuple] with
    def apply(l: EmptyTuple): EmptyTuple = l
  end given

  given [L <: Tuple, MH, MT <: Tuple, R <: Tuple](using select: Remove.Aux[L, MH, (MH, R)], alignTail: Align[R, MT])
  : Align[L, MH *: MT] with
    def apply(l: L): MH *: MT =
      val (h, t) = select(l)
      h *: alignTail(t)
  end given
end Align
