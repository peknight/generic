package com.peknight.generic.deriving

import cats.Applicative
import com.peknight.generic.deriving.Generic
import com.peknight.generic.deriving.tuple.{summonAsTuple, summonValuesAsTuple}
import com.peknight.generic.syntax.tuple.mapN
import com.peknight.generic.ops.tuple.LiftedTuple

sealed trait Generic[F[_], A]:
  type Labels <: Tuple
  type Repr <: Tuple
  type MirrorType <: Mirror.Labelled[A, Labels, Repr]

  def mirror: MirrorType
  def instances: LiftedTuple[F, Repr]
  inline def labels: Labels = summonValuesAsTuple[Labels]

  inline def derive(f: => Generic.Product[F, A] ?=> F[A], g: => Generic.Sum[F, A] ?=> F[A]): F[A] =
    inline this match
      case product: Generic.Product[F, A] => f(using product)
      case sum: Generic.Sum[F, A] => g(using sum)
  end derive
end Generic

object Generic:

  def apply[F[_], A](using generic: Generic[F, A]): Generic[F, A] = generic

  sealed abstract class Aux[F[_], A, MirrorType0 <: Mirror.Labelled[A, Labels0, Repr0], Labels0 <: Tuple, Repr0 <: Tuple](
    val mirror: MirrorType0, inst: () => LiftedTuple[F, Repr0]
  ) extends Generic[F, A]:
    type MirrorType = MirrorType0
    type Labels = Labels0
    type Repr = Repr0
    lazy val instances: LiftedTuple[F, Repr] = inst()
  end Aux

  object Aux:
    def apply[F[_], A, MirrorType <: Mirror.Labelled[A, Labels, Repr], Labels <: Tuple, Repr <: Tuple](
      using generic: Aux[F, A, MirrorType, Labels, Repr]
    ): Aux[F, A, MirrorType, Labels, Repr] = generic
  end Aux

  sealed trait Product[F[_], A] extends Generic[F, A]:
    type MirrorType = Mirror.Product.Labelled[A, Labels, Repr]
    def fromInstances(using Applicative[F]): F[A] = instances.mapN(mirror.fromProduct)
  end Product

  extension [F[_], A <: scala.Product] (generic: Product[F, A])

    inline def zip[G[_]](a: A)(f: [T] => (F[T], T) => G[T]): LiftedTuple[G, generic.Repr] =
      type H[E] = E match
        case (_, t) => G[t]
      generic.instances.zip(Tuple.fromProductTyped(a)(using generic.mirror)).map[H] { [E] => (e: E) =>
        type U = E match
          case (_, t) => t
        val (instance, value) = e.asInstanceOf[(F[U], U)]
        f(instance, value).asInstanceOf[H[E]]
      }.asInstanceOf[LiftedTuple[G, generic.Repr]]

    inline def zipWithLabels[G[_]](a: A)(f: [T] => (F[T], T, String) => G[T]): LiftedTuple[G, generic.Repr] =
      type H[E] = E match
        case ((_, t), _) => G[t]
      generic.instances.zip(Tuple.fromProductTyped(a)(using generic.mirror)).zip(generic.labels).map[H] {
        [E] => (e: E) =>
          type U = E match
            case ((_, t), _) => t
          val ((instance, value), label) = e.asInstanceOf[((F[U], U), String)]
          f(instance, value, label).asInstanceOf[H[E]]
      }.asInstanceOf[LiftedTuple[G, generic.Repr]]
  end extension

  object Product:
    def apply[F[_], A](using generic: Product[F, A]): Product[F, A] = generic

    final class Aux[F[_], A, Labels0 <: Tuple, Repr0 <: Tuple](
      override val mirror: Mirror.Product.Labelled[A, Labels0, Repr0],
      inst: () => LiftedTuple[F, Repr0]
    ) extends Generic.Aux[F, A, Mirror.Product.Labelled[A, Labels0, Repr0], Labels0, Repr0](mirror, inst)
      with Product[F, A]
    object Aux:
      def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Aux[F, A, Labels, Repr])
      : Aux[F, A, Labels, Repr] = generic
    end Aux
  end Product

  sealed trait Sum[F[_], A] extends Generic[F, A]:
    type MirrorType = Mirror.Sum.Labelled[A, Labels, Repr]
    def ordinal(a: A): F[A] = ordinal(mirror.ordinal(a))
    def ordinal(ord: Int): F[A] = instances.productElement(ord).asInstanceOf[F[A]]
  end Sum

  object Sum:
    def apply[F[_], A](using generic: Sum[F, A]): Sum[F, A] = generic

    final class Aux[F[_], A, Labels0 <: Tuple, Repr0 <: Tuple](
      override val mirror: Mirror.Sum.Labelled[A, Labels0, Repr0],
      inst: () => LiftedTuple[F, Repr0]
    ) extends Generic.Aux[F, A, Mirror.Sum.Labelled[A, Labels0, Repr0], Labels0, Repr0](mirror, inst)
      with Sum[F, A]

    object Aux:
      def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Aux[F, A, Labels, Repr])
      : Aux[F, A, Labels, Repr] = generic
    end Aux
  end Sum

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Product.Labelled[A, Labels, Repr]
  ): Generic.Product.Aux[F, A, Labels, Repr] =
    new Generic.Product.Aux[F, A, Labels, Repr](mirror, () => summonAsTuple[LiftedTuple[F, Repr]])

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Sum.Labelled[A, Labels, Repr]
  ): Generic.Sum.Aux[F, A, Labels, Repr] =
    new Generic.Sum.Aux[F, A, Labels, Repr](mirror, () => summonAsTuple[LiftedTuple[F, Repr]])

end Generic
