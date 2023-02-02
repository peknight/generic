package com.peknight.generic.ops.tuple

import com.peknight.generic.constraint.NotContainsConstraint
import com.peknight.generic.dependent.DepFn1

/**
 * Type class supporting `Tuple` intersection. In case of duplicate types, this operation is a multiset intersection.
 * If type `T` appears n times in this `Tuple` and m < n times in `M`, the resulting `Tuple` contains the first m
 * elements of type `T` in this `Tuple`.
 *
 * Also available if `M` contains types absent in this `Tuple`
 *
 * @author Olivier Blanvillain
 * @author Arya Irani
 */
private[generic] trait Intersection[L <: Tuple, M <: Tuple] extends DepFn1[L] with Serializable:
  type Out <: Tuple
end Intersection

private[generic] object Intersection extends LowPriorityIntersection:
  def apply[L <: Tuple, M <: Tuple](using intersection: Intersection[L, M]): Aux[L, M, intersection.Out] =
    intersection

  // let ∅ ∩ M = ∅
  given [M <: Tuple]: Aux[EmptyTuple, M, EmptyTuple] =
    new Intersection[EmptyTuple, M]:
      type Out = EmptyTuple
      def apply(l: EmptyTuple): Out = EmptyTuple
  end given

  // let (H *: T) ∩ M = T ∩ M when H ∉ M
  given [H, T <: Tuple, M <: Tuple, I <: Tuple](using f: NotContainsConstraint[M, H], i: Aux[T, M, I])
  : Aux[H *: T, M, I] =
    new Intersection[H *: T, M]:
      type Out = I
      def apply(l: H *: T): Out = i(l.tail)
  end given

  // let (H *: T) ∩ M  =  H *: (T ∩ (M - H)) when H ∈ M
  given [H, T <: Tuple, M <: Tuple, MR <: Tuple, I <: Tuple](using r: Remove.Aux[M, H, (H, MR)], i: Aux[T, MR, I])
  : Aux[H *: T, M, H *: I] =
    new Intersection[H *: T, M]:
      type Out = H *: I
      def apply(l: H *: T): Out = l.head *: i(l.tail)
  end given
end Intersection
