package com.peknight.generic.scalacheck.instances

import cats.Id
import com.peknight.generic.deriving.Generic
import com.peknight.generic.tuple.syntax.forall
import com.peknight.generic.tuple.{Head, Lifted, Second}
import org.scalacheck.Shrink


//noinspection ScalaDeprecation
trait ShrinkInstances:

  inline given [A](using instances: => Generic.Instances[Shrink, A]): Shrink[A] = instances.derive(
    inst ?=> Shrink[A] { (a: A) =>
      val streamTuple: Lifted[Stream, inst.Repr] = inst.map[Stream](a)(
        [T] => (instance: Shrink[T], t: T) => instance.shrink(t)
      )
      Stream.unfold[A, (A, Lifted[Stream, inst.Repr])]((a, streamTuple)) {
        case (_, streamTuple) if streamTuple.forall { [S] => (s: S) => s.asInstanceOf[Stream[Any]].isEmpty } => None
        case (a, streamTuple) =>
          type Pair[E] = (E, Stream[E])
          val next: Lifted[Pair, inst.Repr] = inst.to(a).zip(streamTuple).map[Id] { [T] => (t: T) =>
            type E = T match { case (E, _) => E }
            t match
              case (_, s: Stream[E]) if s.isEmpty => t
              case (_, s: Stream[E]) => (s.head, s.tail).asInstanceOf[Id[T]]
          }.asInstanceOf[Lifted[Pair, inst.Repr]]
          val nextA = inst.from(next.map[Head] { [T] => (t: T) => t match
            case (e, _) => e.asInstanceOf[Head[T]]
          }.asInstanceOf[inst.Repr])
          val nextS = next.map[Second] { [T] => (t: T) => t match
            case (_, s) => s.asInstanceOf[Second[T]]
          }.asInstanceOf[Lifted[Stream, inst.Repr]]
          Some((nextA, (nextA, nextS)))
      }
    },
    inst ?=> Shrink[A] { (a: A) => inst.instance(a).shrink(a) }
  )

end ShrinkInstances

object ShrinkInstances extends ShrinkInstances