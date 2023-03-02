package com.peknight.generic.tuple.ops

import com.peknight.generic.dependent.DepFn1

/**
 * Type class supporting `Tuple` subtraction. In case of duplicate types, this operation is a multiset difference.
 * If type `T` appears n times in this `Tuple` and m < n times in `M`, the resulting `Tuple` contains the last n - m
 * elements of type `T` in this `Tuple`.
 *
 * Also available if `M` contains types absent in this `Tuple`.
 *
 * @author Olivier Blanvillain
 */
private[generic] trait Diff[L <: Tuple, M <: Tuple] extends DepFn1[L] with Serializable:
  type Out <: Tuple
end Diff

private[generic] object Diff extends Diff2:
  def apply[L <: Tuple, M <: Tuple](using diff: Diff[L, M]): Aux[L, M, diff.Out] = diff

  given [L <: Tuple]: Aux[L, EmptyTuple, L] =
    new Diff[L, EmptyTuple]:
      type Out = L
      def apply(l: L): Out = l
  end given

  given [L <: Tuple, LT <: Tuple, H, T <: Tuple, D <: Tuple](using r: Remove.Aux[L, H, (H, LT)], d: Aux[LT, T, D])
  : Aux[L, H *: T, D] =
    new Diff[L, H *: T]:
      type Out = D
      def apply(l: L): Out = d(r(l)._2)
  end given
end Diff
