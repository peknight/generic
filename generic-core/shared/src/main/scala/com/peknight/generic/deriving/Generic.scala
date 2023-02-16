package com.peknight.generic.deriving

import cats.Applicative
import com.peknight.generic.deriving.Generic
import com.peknight.generic.deriving.tuple.{summonAsTuple, summonValuesAsTuple}
import com.peknight.generic.ops.tuple.LiftedTuple
import com.peknight.generic.syntax.tuple.mapN
import scala.compiletime.constValue
import scala.Tuple.Size

sealed trait Generic[F[_], A]:
  type Labels <: Tuple
  type Repr <: Tuple
  type MirrorType <: Mirror.Labelled[A, Labels, Repr]

  def mirror: MirrorType
  def size: Int
  def labels: Labels
  def instances: LiftedTuple[F, Repr]

  inline def derive(f: => Generic.Product[F, A] ?=> F[A], g: => Generic.Sum[F, A] ?=> F[A]): F[A] =
    inline this match
      case p: Generic.Product[F, A] => f(using p)
      case s: Generic.Sum[F, A] => g(using s)
  end derive
end Generic

object Generic:

  def apply[F[_], A](using generic: Generic[F, A]): Generic[F, A] = generic

  sealed abstract class Aux[F[_], A, MirrorType0 <: Mirror.Labelled[A, Labels0, Repr0], Labels0 <: Tuple, Repr0 <: Tuple](
    val mirror: MirrorType0, size0: () => Int, labels0: () => Labels0, instances0: () => LiftedTuple[F, Repr0]
  ) extends Generic[F, A]:
    type MirrorType = MirrorType0
    type Labels = Labels0
    type Repr = Repr0
    lazy val size: Int = size0()
    lazy val labels: Labels = labels0()
    lazy val instances: LiftedTuple[F, Repr] = instances0()
  end Aux

  object Aux:
    def apply[F[_], A, MirrorType <: Mirror.Labelled[A, Labels, Repr], Labels <: Tuple, Repr <: Tuple](
      using generic: Aux[F, A, MirrorType, Labels, Repr]
    ): Aux[F, A, MirrorType, Labels, Repr] = generic
  end Aux

  sealed trait Product[F[_], A] extends Generic[F, A]:
    type MirrorType = Mirror.Product.Labelled[A, Labels, Repr]
    def toRepr(a: A): Repr
    def fromInstances(using Applicative[F]): F[A] = instances.mapN(mirror.fromProduct)

    def map[G[_]](a: A)(f: [T] => (F[T], T) => G[T]): LiftedTuple[G, Repr] =
      type H[E] = E match { case (_, t) => G[t] }
      instances.zip(toRepr(a)).map[H] { [E] => (e: E) =>
        type U = E match { case (_, t) => t }
        val (instance, value) = e.asInstanceOf[(F[U], U)]
        f(instance, value).asInstanceOf[H[E]]
      }.asInstanceOf[LiftedTuple[G, Repr]]

    def mapWithLabel[G[_]](a: A)(f: [T] => (F[T], T, String) => G[T]): LiftedTuple[G, Repr] =
      type H[E] = E match { case ((_, t), _) => G[t] }
      instances.zip(toRepr(a)).zip(labels).map[H] { [E] => (e: E) =>
        type U = E match { case ((_, t), _) => t }
        val ((instance, value), label) = e.asInstanceOf[((F[U], U), String)]
        f(instance, value, label).asInstanceOf[H[E]]
      }.asInstanceOf[LiftedTuple[G, Repr]]
  end Product

  object Product:
    def apply[F[_], A](using generic: Product[F, A]): Product[F, A] = generic

    abstract class Aux[F[_], A, Labels0 <: Tuple, Repr0 <: Tuple](
      override val mirror: Mirror.Product.Labelled[A, Labels0, Repr0],
      size0: () => Int,
      labels0: () => Labels0,
      instances0: () => LiftedTuple[F, Repr0]
    ) extends Generic.Aux[F, A, Mirror.Product.Labelled[A, Labels0, Repr0], Labels0, Repr0](
      mirror, size0, labels0, instances0
    ) with Product[F, A]
    object Aux:
      def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Aux[F, A, Labels, Repr])
      : Aux[F, A, Labels, Repr] = generic
    end Aux
  end Product

  sealed trait Sum[F[_], A] extends Generic[F, A]:
    type MirrorType = Mirror.Sum.Labelled[A, Labels, Repr]
    def ordinal(a: A): Int = mirror.ordinal(a)
    def instance(a: A): F[A] = instance(ordinal(a))
    def instance(ord: Int): F[A] = instances.productElement(ord).asInstanceOf[F[A]]
  end Sum

  object Sum:
    def apply[F[_], A](using generic: Sum[F, A]): Sum[F, A] = generic

    final class Aux[F[_], A, Labels0 <: Tuple, Repr0 <: Tuple](
      override val mirror: Mirror.Sum.Labelled[A, Labels0, Repr0],
      size0: () => Int,
      labels0: () => Labels0,
      instances0: () => LiftedTuple[F, Repr0]
    ) extends Generic.Aux[F, A, Mirror.Sum.Labelled[A, Labels0, Repr0], Labels0, Repr0](
      mirror, size0, labels0, instances0
    ) with Sum[F, A]

    object Aux:
      def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Aux[F, A, Labels, Repr])
      : Aux[F, A, Labels, Repr] = generic
    end Aux
  end Sum

  inline given [F[_], A <: scala.Product, Labels <: Tuple, Repr0 <: Tuple](
    using mirror: Mirror.Product.Labelled[A, Labels, Repr0]
  ): Generic.Product.Aux[F, A, Labels, Repr0] =
    new Generic.Product.Aux[F, A, Labels, Repr0](mirror, () => constValue[Size[Repr0]],
      () => summonValuesAsTuple[Labels], () => summonAsTuple[LiftedTuple[F, Repr0]]):
      def toRepr(a: A): Repr = Tuple.fromProduct(a).asInstanceOf[Repr]

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Sum.Labelled[A, Labels, Repr]
  ): Generic.Sum.Aux[F, A, Labels, Repr] =
    new Generic.Sum.Aux[F, A, Labels, Repr](mirror, () => constValue[Size[Repr]],
      () => summonValuesAsTuple[Labels], () => summonAsTuple[LiftedTuple[F, Repr]])

end Generic
