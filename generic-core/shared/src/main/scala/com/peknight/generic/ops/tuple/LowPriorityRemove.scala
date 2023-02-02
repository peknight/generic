package com.peknight.generic.ops.tuple

private[generic] trait LowPriorityRemove:
  type Aux[L <: Tuple, E, Out0] = Remove[L, E] { type Out = Out0 }

  given [H, T <: Tuple, E, OutT <: Tuple](using r: Aux[T, E, (E, OutT)]): Aux[H *: T, E, (E, H *: OutT)] =
    new Remove[H *: T, E]:
      type Out = (E, H *: OutT)

      def apply(l: H *: T): Out =
        val (e, tail) = r(l.tail)
        (e, l.head *: tail)

      def reinsert(out: Out): H *: T = out._2.head *: r.reinsert((out._1, out._2.tail))
  end given
end LowPriorityRemove
