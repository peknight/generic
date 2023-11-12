package com.peknight.generic.monocle.instances

import cats.{Functor, Id}
import com.peknight.generic.Mirror
import com.peknight.generic.tuple.ops.Remove
import monocle.Lens

trait LensInstances:
  given [A <: Product, Repr <: Tuple, B, Rest <: Tuple](using mirror: Mirror.Product.Aux[A, Repr],
                                                        remove: Remove.Aux[Repr, B, (B, Rest)]): Lens[A, B] with
    def get(s: A): B = remove.apply(Tuple.fromProductTyped(s))._1
    def modifyF[F[_] : Functor](f: B => F[B])(s: A): F[A] =
      val (b, rest) = remove.apply(Tuple.fromProductTyped(s))
      Functor[F].map[B, A](f(b))(bb => mirror.fromProduct(remove.reinsert((bb, rest))))
    override def modify(f: B => B): A => A = modifyF[Id](f)
    override def replace(b: B): A => A = modifyF[Id](_ => b)
  end given

end LensInstances
object LensInstances extends LensInstances
