package com.peknight.generic.scalacheck.instances

import cats.Id
import com.peknight.generic.deriving.Generic
import com.peknight.generic.ops.tuple.{Head, LiftedTuple, SecondElem}
import com.peknight.generic.syntax.tuple.forall
import org.scalacheck.Shrink


//noinspection ScalaDeprecation
trait ShrinkInstances:

  inline given [A](using instances: => Generic.Instances[Shrink, A]): Shrink[A] = instances.derive(
    inst ?=> Shrink[A] { (a: A) =>
      val streamTuple: LiftedTuple[Stream, inst.Repr] = inst.map[Stream](a)(
        [T] => (instance: Shrink[T], t: T) => instance.shrink(t)
      )
      Stream.unfold[A, (A, LiftedTuple[Stream, inst.Repr])]((a, streamTuple)) {
        case (_, streamTuple) if streamTuple.forall { [S] => (s: S) => s.asInstanceOf[Stream[Any]].isEmpty } => None
        case (a, streamTuple) =>
          type Pair[E] = (E, Stream[E])
          val next: LiftedTuple[Pair, inst.Repr] = inst.to(a).zip(streamTuple).map[Id] { [T] => (t: T) =>
            type E = T match { case (E, _) => E }
            t match
              case (_, s: Stream[E]) if s.isEmpty => t
              case (_, s: Stream[E]) => (s.head, s.tail).asInstanceOf[Id[T]]
          }.asInstanceOf[LiftedTuple[Pair, inst.Repr]]
          val nextA = inst.from(next.map[Head] { [T] => (t: T) => t match
            case (e, _) => e.asInstanceOf[Head[T]]
          }.asInstanceOf[inst.Repr])
          val nextS = next.map[SecondElem] { [T] => (t: T) => t match
            case (_, s) => s.asInstanceOf[SecondElem[T]]
          }.asInstanceOf[LiftedTuple[Stream, inst.Repr]]
          Some((nextA, (nextA, nextS)))
      }
    },
    inst ?=> Shrink[A] { (a: A) => inst.instance(a).shrink(a) }
  )

end ShrinkInstances

object ShrinkInstances extends ShrinkInstances