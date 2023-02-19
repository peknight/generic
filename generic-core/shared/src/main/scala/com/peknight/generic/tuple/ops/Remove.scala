package com.peknight.generic.tuple.ops

import com.peknight.generic.dependent.DepFn1

/**
 * Type class supporting removal of an element from this `HList`. Available only if this `HList` contains an
 * element of type `E`.
 *
 * @author Stacy Curl
 */
private[generic] trait Remove[L <: Tuple, E] extends DepFn1[L] with Serializable:
  def reinsert(out: Out): L
end Remove

private[generic] object Remove extends LowPriorityRemove:
  def apply[L <: Tuple, E](using remove: Remove[L, E]): Aux[L, E, remove.Out] = remove

  given [H, T <: Tuple]: Aux[H *: T, H, (H, T)] =
    new Remove[H *: T, H]:
      type Out = (H, T)
      def apply(l: H *: T): Out = (l.head, l.tail)
      def reinsert(out: Out): H *: T = out._1 *: out._2
  end given
end Remove

