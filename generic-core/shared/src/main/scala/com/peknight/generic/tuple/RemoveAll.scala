package com.peknight.generic.tuple

import com.peknight.generic.dependent.DepFn1

/**
 * Type class supporting removal of a sublist from this `HList`. Available only if this `HList` contains a
 * sublist of type `SL`.
 *
 * The elements of `SL` do not have to be contiguous in this `HList`.
 *
 * @author Stacy Curl
 */
private[generic] trait RemoveAll[L <: Tuple, SL <: Tuple] extends DepFn1[L] with Serializable:
  def reinsert(out: Out): L
end RemoveAll

private[generic] object RemoveAll:
  type Aux[L <: Tuple, SL <: Tuple, Out0] = RemoveAll[L, SL] {type Out = Out0}

  def apply[L <: Tuple, SL <: Tuple](using remove: RemoveAll[L, SL]): Aux[L, SL, remove.Out] = remove

  given [L <: Tuple]: Aux[L, EmptyTuple, (EmptyTuple, L)] =
    new RemoveAll[L, EmptyTuple]:
      type Out = (EmptyTuple, L)
      def apply(l: L): Out = (EmptyTuple, l)
      def reinsert(out: (EmptyTuple, L)): L = out._2
  end given

  given [L <: Tuple, E, RemE <: Tuple, Rem <: Tuple, SLT <: Tuple]
  (using rt: Remove.Aux[L, E, (E, RemE)], st: Aux[RemE, SLT, (SLT, Rem)]): Aux[L, E *: SLT, (E *: SLT, Rem)] =
    new RemoveAll[L, E *: SLT]:
      type Out = (E *: SLT, Rem)

      def apply(l: L): Out =
        val (e, rem) = rt(l)
        val (sl, left) = st(rem)
        (e *: sl, left)

      def reinsert(out: (E *: SLT, Rem)): L =
        rt.reinsert((out._1.head, st.reinsert((out._1.tail, out._2))))
  end given
end RemoveAll
