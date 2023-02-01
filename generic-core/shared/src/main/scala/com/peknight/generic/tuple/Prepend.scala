package com.peknight.generic.tuple

import com.peknight.generic.dependent.DepFn2

/**
 * Type class supporting prepending to this `Tuple`.
 *
 * @author Miles Sabin
 */
private[generic] trait Prepend[P <: Tuple, S <: Tuple] extends DepFn2[P, S] with Serializable:
  type Out <: Tuple
end Prepend

private[generic] object Prepend extends LowPriorityPrepend:
  def apply[P <: Tuple, S <: Tuple](using prepend: Prepend[P, S]): Aux[P, S, prepend.Out] = prepend

  given [P <: EmptyTuple, S <: Tuple]: Aux[P, S, S] =
    new Prepend[P, S]:
      type Out = S
      def apply(prefix: P, suffix: S): S = suffix
  end given
end Prepend
