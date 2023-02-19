package com.peknight.generic.deriving

import cats.Applicative
import com.peknight.generic.compiletime.{summonAsTuple, summonValuesAsTuple}
import com.peknight.generic.tuple.Lifted
import com.peknight.generic.tuple.syntax.{foldLeft, mapN}

import scala.Tuple.Size
import scala.compiletime.constValue

sealed trait Generic[A]:
  type Labels <: Tuple
  type Repr <: Tuple
  type MirrorType <: Mirror.Labelled[A, Labels, Repr]

  def mirror: MirrorType
  def size: Int
  def labels: Labels
end Generic

object Generic:
  def apply[A](using generic: Generic[A]): Generic[A] = generic

  sealed abstract class Aux[A, Labels0 <: Tuple, Repr0 <: Tuple, MirrorType0 <: Mirror.Labelled[A, Labels0, Repr0]](
    val mirror: MirrorType0, val size: Int, val labels: Labels0
  ) extends Generic[A]:
    type Labels = Labels0
    type Repr = Repr0
    type MirrorType = MirrorType0
  end Aux

  object Aux:
    def apply[A, Labels <: Tuple, Repr <: Tuple, MirrorType <: Mirror.Labelled[A, Labels, Repr]](
      using generic: Generic.Aux[A, Labels, Repr, MirrorType]
    ): Aux[A, Labels, Repr, MirrorType] = generic
  end Aux

  sealed trait Instances[F[_], A]:
    type Labels <: Tuple
    type Repr <: Tuple
    type MirrorType <: Mirror.Labelled[A, Labels, Repr]
    type GenericType <: Generic.Aux[A, Labels, Repr, MirrorType]
    def generic: GenericType
    def instances: Lifted[F, Repr]

    def mirror: MirrorType = generic.mirror
    def size: Int = generic.size
    def labels: Labels = generic.labels

    inline def derive(f: => Generic.Product.Instances[F, A] ?=> F[A], g: => Generic.Sum.Instances[F, A] ?=> F[A]): F[A] =
      inline this match
        case p: Generic.Product.Instances[F, A] => f(using p)
        case s: Generic.Sum.Instances[F, A] => g(using s)
    end derive
  end Instances

  object Instances:
    def apply[F[_], A](using inst: Instances[F, A]): Generic.Instances[F, A] = inst
    sealed abstract class Aux[F[_], A, Labels0 <: Tuple, Repr0 <: Tuple,
      MirrorType0 <: Mirror.Labelled[A, Labels0, Repr0], GenericType0 <: Generic.Aux[A, Labels0, Repr0, MirrorType0]](
      val generic: GenericType0, instances0: () => Lifted[F, Repr0]
    ) extends Generic.Instances[F, A]:
      type Labels = Labels0
      type Repr = Repr0
      type MirrorType = MirrorType0
      type GenericType = GenericType0
      lazy val instances: Lifted[F, Repr] = instances0()
    end Aux
    object Aux:
      def apply[F[_], A, Labels <: Tuple, Repr <: Tuple, MirrorType <: Mirror.Labelled[A, Labels, Repr],
        GenericType <: Generic.Aux[A, Labels, Repr, MirrorType]](
        using inst: Generic.Instances.Aux[F, A, Labels, Repr, MirrorType, GenericType]
      ): Aux[F, A, Labels, Repr, MirrorType, GenericType] = inst
    end Aux
  end Instances

  sealed trait Product[A] extends Generic[A]:
    type MirrorType = Mirror.Product.Labelled[A, Labels, Repr]
    def to(a: A): Repr
    def from(repr: Repr): A = mirror.fromProduct(repr)
  end Product

  object Product:
    def apply[A](using generic: Generic.Product[A]): Generic.Product[A] = generic

    abstract class Aux[A, Labels0 <: Tuple, Repr0 <: Tuple](
      override val mirror: Mirror.Product.Labelled[A, Labels0, Repr0],
      override val size: Int,
      override val labels: Labels0
    ) extends Generic.Aux[A, Labels0, Repr0, Mirror.Product.Labelled[A, Labels0, Repr0]](
      mirror, size, labels
    ) with Generic.Product[A]

    object Aux:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Product.Aux[A, Labels, Repr])
      : Generic.Product.Aux[A, Labels, Repr] = generic
    end Aux

    sealed trait Instances[F[_], A] extends Generic.Instances[F, A]:
      type MirrorType = Mirror.Product.Labelled[A, Labels, Repr]
      type GenericType = Generic.Product.Aux[A, Labels, Repr]
      def fromInstances(using Applicative[F]): F[A] = instances.mapN(from)
      def map[G[_]](a: A)(f: [T] => (F[T], T) => G[T]): Lifted[G, Repr] =
        type H[E] = E match { case (_, t) => G[t] }
        instances.zip(to(a)).map[H] {
          [E] => (e: E) =>
            type U = E match { case (_, t) => t }
            val (instance, value) = e.asInstanceOf[(F[U], U)]
            f(instance, value).asInstanceOf[H[E]]
        }.asInstanceOf[Lifted[G, Repr]]
      def mapWithLabel[G[_]](a: A)(f: [T] => (F[T], T, String) => G[T]): Lifted[G, Repr] =
        type H[E] = E match { case ((_, t), _) => G[t] }
        instances.zip(to(a)).zip(labels).map[H] {
          [E] => (e: E) =>
            type U = E match { case ((_, t), _) => t }
            val ((instance, value), label) = e.asInstanceOf[((F[U], U), String)]
            f(instance, value, label).asInstanceOf[H[E]]
        }.asInstanceOf[Lifted[G, Repr]]
      def foldLeft[B](a: A)(b: B)(f: [T] => (B, F[T], T) => B): B =
        instances.zip(to(a)).foldLeft[B](b) { [E] => (b: B, e: E) =>
          type U = E match { case (_, t) => t }
          val (instance, value) = e.asInstanceOf[(F[U], U)]
          f(b, instance, value)
        }
      def foldLeftWithLabel[B](a: A)(b: B)(f: [T] => (B, F[T], T, String) => B): B =
        instances.zip(to(a)).zip(labels).foldLeft[B](b) { [E] => (b: B, e: E) =>
          type U = E match { case ((_, t), _) => t }
          val ((instance, value), label) = e.asInstanceOf[((F[U], U), String)]
          f(b, instance, value, label)
        }
      def to(a: A): Repr = generic.to(a)
      def from(repr: Repr): A = generic.from(repr)
    end Instances
    object Instances:
      def apply[F[_], A](using inst: Generic.Product.Instances[F, A]): Generic.Product.Instances[F, A] = inst
      final class Aux[F[_], A, Labels <: Tuple, Repr <: Tuple](
        override val generic: Generic.Product.Aux[A, Labels, Repr],
        instances0: () => Lifted[F, Repr]
      ) extends Generic.Instances.Aux[F, A, Labels, Repr, Mirror.Product.Labelled[A, Labels, Repr],
        Generic.Product.Aux[A, Labels, Repr]](generic, instances0) with Generic.Product.Instances[F, A]
      object Aux:
        def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](
          using inst: Generic.Product.Instances.Aux[F, A, Labels, Repr]
        ): Generic.Product.Instances.Aux[F, A, Labels, Repr] = inst
      end Aux
    end Instances
  end Product

  sealed trait Sum[A] extends Generic[A]:
    type MirrorType = Mirror.Sum.Labelled[A, Labels, Repr]
    def ordinal(a: A): Int = mirror.ordinal(a)
    def label(ord: Int): String = labels.productElement(ord).asInstanceOf[String]
    def label(a: A): String = label(ordinal(a))
  end Sum

  object Sum:
    def apply[A](using generic: Generic.Sum[A]): Generic.Sum[A] = generic

    final class Aux[A, Labels0 <: Tuple, Repr0 <: Tuple](
      override val mirror: Mirror.Sum.Labelled[A, Labels0, Repr0],
      override val size: Int,
      override val labels: Labels0
    ) extends Generic.Aux[A, Labels0, Repr0, Mirror.Sum.Labelled[A, Labels0, Repr0]](
      mirror, size, labels
    ) with Generic.Sum[A]

    object Aux:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Sum.Aux[A, Labels, Repr])
      : Generic.Sum.Aux[A, Labels, Repr] = generic
    end Aux

    sealed trait Instances[F[_], A] extends Generic.Instances[F, A]:
      type MirrorType = Mirror.Sum.Labelled[A, Labels, Repr]
      type GenericType = Generic.Sum.Aux[A, Labels, Repr]
      def instance(ord: Int): F[A] = instances.productElement(ord).asInstanceOf[F[A]]
      def instance(a: A): F[A] = instance(ordinal(a))

      def withInstance[B](a: A)(f: F[A] => B): B = f(instance(a))

      def withLabel[B](a: A)(f: (F[A], String) => B): B =
        val ord = ordinal(a)
        f(instance(ord), label(ord))

      def ordinal(a: A): Int = generic.ordinal(a)
      def label(ord: Int): String = generic.label(ord)
      def label(a: A): String = generic.label(a)

    end Instances

    object Instances:
      def apply[F[_], A](using inst: Generic.Sum.Instances[F, A]): Generic.Sum.Instances[F, A] = inst
      final class Aux[F[_], A, Labels <: Tuple, Repr <: Tuple](
        override val generic: Generic.Sum.Aux[A, Labels, Repr],
        instances0: () => Lifted[F, Repr]
      ) extends Generic.Instances.Aux[F, A, Labels, Repr, Mirror.Sum.Labelled[A, Labels, Repr],
        Generic.Sum.Aux[A, Labels, Repr]](generic, instances0) with Generic.Sum.Instances[F, A]
      object Aux:
        def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](using inst: Generic.Sum.Instances.Aux[F, A, Labels, Repr])
        : Generic.Sum.Instances.Aux[F, A, Labels, Repr] = inst
      end Aux
    end Instances
  end Sum

  inline given [A <: scala.Product, Labels <: Tuple, Repr0 <: Tuple](
    using mirror: Mirror.Product.Labelled[A, Labels, Repr0]
  ): Generic.Product.Aux[A, Labels, Repr0] =
    new Generic.Product.Aux[A, Labels, Repr0](mirror, constValue[Size[Repr0]], summonValuesAsTuple[Labels]):
      def to(a: A): Repr = Tuple.fromProduct(a).asInstanceOf[Repr]

  inline given [A, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Sum.Labelled[A, Labels, Repr]
  ): Generic.Sum.Aux[A, Labels, Repr] =
    new Generic.Sum.Aux[A, Labels, Repr](mirror, constValue[Size[Repr]], summonValuesAsTuple[Labels])

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Product.Aux[A, Labels, Repr])
  : Generic.Product.Instances.Aux[F, A, Labels, Repr] =
    new Generic.Product.Instances.Aux[F, A, Labels, Repr](generic, () => summonAsTuple[Lifted[F, Repr]])

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Sum.Aux[A, Labels, Repr])
  : Generic.Sum.Instances.Aux[F, A, Labels, Repr] =
    new Generic.Sum.Instances.Aux[F, A, Labels, Repr](generic, () => summonAsTuple[Lifted[F, Repr]])
end Generic
