package com.peknight.generic.tuple.ops

import com.peknight.generic.dependent.DepFn1

/**
 * Type class supporting multiple HList field selection.
 * Can be used to witness that given HList contains certain set of field types.
 * Simplified version of shapeless.ops.record.SelectAll
 *
 * @author Ievgen Garkusha
 */
private[generic] trait SelectAll[L <: Tuple, S <: Tuple] extends DepFn1[L] with Serializable:
  type Out = S
end SelectAll

private[generic] object SelectAll:
  def apply[L <: Tuple, S <: Tuple](using select: SelectAll[L, S]): SelectAll[L, S] = select

  given [L <: Tuple]: SelectAll[L, EmptyTuple] =
    new SelectAll[L, EmptyTuple]:
      def apply(l: L): EmptyTuple = EmptyTuple
  end given

  given[L <: Tuple, H, S <: Tuple](using sh: Selector[L, H], st: SelectAll[L, S]): SelectAll[L, H *: S] =
    new SelectAll[L, H *: S]:
      def apply(l: L): H *: S = sh(l) *: st(l)
  end given
end SelectAll
