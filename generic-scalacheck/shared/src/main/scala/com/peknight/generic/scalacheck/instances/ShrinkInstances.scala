package com.peknight.generic.scalacheck.instances

import cats.Id
import com.peknight.generic.deriving.Generic
import com.peknight.generic.tuple.syntax.{forall, unzip}
import com.peknight.generic.tuple.{Head, Map, Second}
import org.scalacheck.Shrink

import scala.Tuple.Zip


//noinspection ScalaDeprecation
trait ShrinkInstances:

  inline given [A](using instances: => Generic.Instances[Shrink, A]): Shrink[A] = instances.derive(
    inst ?=> Shrink[A] { (a: A) =>
      type Repr = inst.Repr
      val streamTuple: Map[Repr, Stream] = inst.map[Stream](a)(
        [T] => (instance: Shrink[T], t: T) => instance.shrink(t)
      )
      Stream.unfold[Repr, (Repr, Map[Repr, Stream])]((inst.to(a), streamTuple)) {
        case (_, streamTuple) if streamTuple.forall { [T] => (t: T) => t.asInstanceOf[Stream[Any]].isEmpty } => None
        case (repr, streamTuple) =>
          val (nextRepr, nextS) = repr.zip(streamTuple).map[Id] { [T] => (t: T) =>
            t match
              case (_, s: Stream[_]) if s.isEmpty => t
              case (_, s: Stream[_]) => (s.head, s.tail).asInstanceOf[Id[T]]
          }.asInstanceOf[Zip[Repr, Map[Repr, Stream]]].unzip
          Some((nextRepr, (nextRepr, nextS)))
      }.map(inst.from)
    },
    inst ?=> Shrink[A] { (a: A) => inst.instance(a).shrink(a) }
  )

end ShrinkInstances

object ShrinkInstances extends ShrinkInstances