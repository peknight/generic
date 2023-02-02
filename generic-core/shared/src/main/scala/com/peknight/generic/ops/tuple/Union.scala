package com.peknight.generic.ops.tuple

import com.peknight.generic.constraint.NotContainsConstraint
import com.peknight.generic.dependent.DepFn2

/**
 * Type class supporting `Tuple` union. In case of duplicate types, this operation is a order-preserving multi-set union.
 * If type `T` appears n times in this `Tuple` and m > n times in `M`, the resulting `Tuple` contains the first n elements
 * of type `T` in this `Tuple`, followed by the last m - n element of type `T` in `M`.
 *
 * @author Olivier Blanvillain
 * @author Arya Irani
 */
private[generic] trait Union[L <: Tuple, M <: Tuple] extends DepFn2[L, M] with Serializable:
  type Out <: Tuple
end Union

private[generic] object Union extends LowPriorityUnion:
  def apply[L <: Tuple, M <: Tuple](using union: Union[L, M]): Aux[L, M, union.Out] = union

  // let ∅ ∪ M = M
  given [M <: Tuple]: Aux[EmptyTuple, M, M] =
    new Union[EmptyTuple, M]:
      type Out = M
      def apply(l: EmptyTuple, m: M): Out = m
  end given

  // let (H *: T) ∪ M = H *: (T ∪ M) when H ∉ M
  given [H, T <: Tuple, M <: Tuple, U <: Tuple](using f: NotContainsConstraint[M, H], u: Aux[T, M, U])
  : Aux[H *: T, M, H *: U] =
    new Union[H *: T, M]:
      type Out = H *: U
      def apply(l: H *: T, m: M): Out = l.head *: u(l.tail, m)
  end given

  // let (H *: T) ∪ M  =  H *: (T ∪ (M - H)) when H ∈ M
  given [H, T <: Tuple, M <: Tuple, MR <: Tuple, U <: Tuple](using r: Remove.Aux[M, H, (H, MR)], u: Aux[T, MR, U])
  : Aux[H *: T, M, H *: U] =
    new Union[H *: T, M]:
      type Out = H *: U
      def apply(l: H *: T, m: M): Out = l.head *: u(l.tail, r(m)._2)
  end given
end Union
