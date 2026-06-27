package com.peknight.generic.tuple.ops

import com.peknight.generic.dependent.DepFn1

/**
 * Type class supporting access to the first element of this `HList` of type `U`. Available only if this `HList`
 * contains an element of type `U`.
 *
 * @author Miles Sabin
 */
private[generic] trait Selector[L <: Tuple, U] extends DepFn1[L] with Serializable:
  type Out = U
end Selector

private[generic] object Selector:
  def apply[L <: Tuple, U](using selector: Selector[L, U]): Selector[L, U] = selector

  given [H, T <: Tuple]: Selector[H *: T, H] =
    new Selector[H *: T, H]:
      def apply(l: H *: T): H = l.head
  end given

  given [H, T <: Tuple, U](using st: Selector[T, U]): Selector[H *: T, U] =
    new Selector[H *: T, U]:
      def apply(l: H *: T): U = st(l.tail)
  end given
end Selector
