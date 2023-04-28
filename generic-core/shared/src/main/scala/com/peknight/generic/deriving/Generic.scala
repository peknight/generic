package com.peknight.generic.deriving

import cats.Applicative
import com.peknight.generic.tuple.Map
import com.peknight.generic.tuple.syntax.{foldLeft, mapN}

import scala.Tuple.Size
import scala.compiletime.{constValue, constValueTuple, summonAll}

sealed trait Generic[A]:
  type Labels <: Tuple
  type Repr <: Tuple
  def size: Int
  def label: String
  def labels: Labels
end Generic

object Generic:
  def apply[A](using generic: Generic[A]): Generic[A] = generic

  type Aux[A, Repr0 <: Tuple] = Generic[A] { type Repr = Repr0 }

  sealed abstract class Labelled[A, Labels0 <: Tuple, Repr0 <: Tuple](
    val size: Int, val label: String, val labels: Labels0
  )extends Generic[A]:
    type Labels = Labels0
    type Repr = Repr0
  end Labelled

  object Labelled:
    def apply[A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Labelled[A, Labels, Repr])
    : Labelled[A, Labels, Repr] = generic
  end Labelled

  sealed trait Instances[F[_], A]:
    type Labels <: Tuple
    type Repr <: Tuple
    type GenericType <: Generic.Labelled[A, Labels, Repr]
    def generic: GenericType
    def instances: Map[Repr, F]
    def size: Int = generic.size
    def label: String = generic.label
    def labels: Labels = generic.labels
    inline def derive(f: => Generic.Product.Instances[F, A] ?=> F[A], g: => Generic.Sum.Instances[F, A] ?=> F[A]): F[A] =
      inline this match
        case p: Generic.Product.Instances[F, A] => f(using p)
        case s: Generic.Sum.Instances[F, A] => g(using s)
    end derive
  end Instances

  object Instances:
    def apply[F[_], A](using inst: Instances[F, A]): Generic.Instances[F, A] = inst

    type Aux[F[_], A, Repr0 <: Tuple] = Generic.Instances[F, A] { type Repr = Repr0 }

    sealed abstract class Labelled[F[_], A, Labels0 <: Tuple, Repr0 <: Tuple,
      GenericType0 <: Generic.Labelled[A, Labels0, Repr0]](val generic: GenericType0, instances0: () => Map[Repr0, F])
      extends Generic.Instances[F, A]:
      type Labels = Labels0
      type Repr = Repr0
      type GenericType = GenericType0
      lazy val instances: Map[Repr, F] = instances0()
    end Labelled
    object Labelled:
      def apply[F[_], A, Labels <: Tuple, Repr <: Tuple, GenericType <: Generic.Labelled[A, Labels, Repr]](
        using inst: Generic.Instances.Labelled[F, A, Labels, Repr, GenericType]
      ): Labelled[F, A, Labels, Repr, GenericType] = inst
    end Labelled
  end Instances

  sealed trait Product[A] extends Generic[A]:
    def to(a: A): Repr
    def from(repr: Repr): A
  end Product

  object Product:
    def apply[A](using generic: Generic.Product[A]): Generic.Product[A] = generic

    type Aux[A, Repr0 <: Tuple] = Generic.Product[A] { type Repr = Repr0 }

    sealed abstract class Labelled[A, Labels <: Tuple, Repr <: Tuple](override val size: Int,
      override val label: String, override val labels: Labels
    ) extends Generic.Labelled[A, Labels, Repr](size, label, labels) with Generic.Product[A]

    final class MirrorLabelled[A <: scala.Product, Labels <: Tuple, Repr <: Tuple](
      mirror: Mirror.Product.Labelled[A, Labels, Repr],
      override val size: Int,
      override val label: String,
      override val labels: Labels
    ) extends Generic.Product.Labelled[A, Labels, Repr](size, label, labels):
      def to(a: A): Repr = Tuple.fromProduct(a).asInstanceOf[Repr]
      def from(repr: Repr): A = mirror.fromProduct(repr)
    end MirrorLabelled

    object Labelled:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Product.Labelled[A, Labels, Repr])
      : Generic.Product.Labelled[A, Labels, Repr] = generic
    end Labelled

    sealed trait Instances[F[_], A] extends Generic.Instances[F, A]:
      type GenericType = Generic.Product.Labelled[A, Labels, Repr]
      def fromInstances(using Applicative[F]): F[A] = instances.mapN(from)
      def map[G[_]](a: A)(f: [T] => (F[T], T) => G[T]): Map[Repr, G] =
        type H[E] = E match { case (_, t) => G[t] }
        instances.zip(to(a)).map[H] {
          [E] => (e: E) =>
            type U = E match { case (_, t) => t }
            val (instance, value) = e.asInstanceOf[(F[U], U)]
            f(instance, value).asInstanceOf[H[E]]
        }.asInstanceOf[Map[Repr, G]]
      def mapWithLabel[G[_]](a: A)(f: [T] => (F[T], T, String) => G[T]): Map[Repr, G] =
        type H[E] = E match { case ((_, t), _) => G[t] }
        instances.zip(to(a)).zip(labels).map[H] {
          [E] => (e: E) =>
            type U = E match { case ((_, t), _) => t }
            val ((instance, value), label) = e.asInstanceOf[((F[U], U), String)]
            f(instance, value, label).asInstanceOf[H[E]]
        }.asInstanceOf[Map[Repr, G]]
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

      type Aux[F[_], A, Repr0 <: Tuple] = Generic.Product.Instances[F, A] { type Repr = Repr0 }

      final class Labelled[F[_], A, Labels <: Tuple, Repr <: Tuple](
        override val generic: Generic.Product.Labelled[A, Labels, Repr],
        instances0: () => Map[Repr, F]
      ) extends Generic.Instances.Labelled[F, A, Labels, Repr, Generic.Product.Labelled[A, Labels, Repr]](
        generic, instances0
      ) with Generic.Product.Instances[F, A]
      object Labelled:
        def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](
          using inst: Generic.Product.Instances.Labelled[F, A, Labels, Repr]
        ): Generic.Product.Instances.Labelled[F, A, Labels, Repr] = inst
      end Labelled
    end Instances
  end Product

  sealed trait Sum[A] extends Generic[A]:
    def ordinal(a: A): Int
    def label(ord: Int): String = labels.productElement(ord).asInstanceOf[String]
    def label(a: A): String = label(ordinal(a))
  end Sum

  object Sum:
    def apply[A](using generic: Generic.Sum[A]): Generic.Sum[A] = generic

    type Aux[A, Repr0 <: Tuple] = Generic.Sum[A] { type Repr = Repr0 }

    sealed abstract class Labelled[A, Labels <: Tuple, Repr <: Tuple](
      override val size: Int,
      override val label: String,
      override val labels: Labels
    ) extends Generic.Labelled[A, Labels, Repr](
      size, label, labels
    ) with Generic.Sum[A]

    final class MirrorLabelled[A, Labels <: Tuple, Repr <: Tuple](
      mirror: Mirror.Sum.Labelled[A, Labels, Repr],
      override val size: Int,
      override val label: String,
      override val labels: Labels
    ) extends Generic.Sum.Labelled[A, Labels, Repr](size, label, labels):
      def ordinal(a: A): Int = mirror.ordinal(a)
    end MirrorLabelled

    object Labelled:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Sum.Labelled[A, Labels, Repr])
      : Generic.Sum.Labelled[A, Labels, Repr] = generic
    end Labelled

    sealed trait Instances[F[_], A] extends Generic.Instances[F, A]:
      type GenericType = Generic.Sum.Labelled[A, Labels, Repr]
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

      type Aux[F[_], A, Repr0 <: Tuple] = Generic.Sum.Instances[F, A] { type Repr = Repr0 }

      final class Labelled[F[_], A, Labels <: Tuple, Repr <: Tuple](
        override val generic: Generic.Sum.Labelled[A, Labels, Repr],
        instances0: () => Map[Repr, F]
      ) extends Generic.Instances.Labelled[F, A, Labels, Repr, Generic.Sum.Labelled[A, Labels, Repr]](
        generic, instances0
      ) with Generic.Sum.Instances[F, A]
      object Labelled:
        def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](using inst: Generic.Sum.Instances.Labelled[F, A, Labels, Repr])
        : Generic.Sum.Instances.Labelled[F, A, Labels, Repr] = inst
      end Labelled
    end Instances
  end Sum

  inline given [A <: scala.Product, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Product.Labelled[A, Labels, Repr]
  ): Generic.Product.MirrorLabelled[A, Labels, Repr] =
    new Generic.Product.MirrorLabelled[A, Labels, Repr](mirror, constValue[Size[Repr]], constValue[mirror.MirroredLabel],
      constValueTuple[Labels])

  inline given [A, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Sum.Labelled[A, Labels, Repr]
  ): Generic.Sum.MirrorLabelled[A, Labels, Repr] =
    new Generic.Sum.MirrorLabelled[A, Labels, Repr](mirror, constValue[Size[Repr]], constValue[mirror.MirroredLabel],
      constValueTuple[Labels])

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Product.Labelled[A, Labels, Repr])
  : Generic.Product.Instances.Labelled[F, A, Labels, Repr] =
    new Generic.Product.Instances.Labelled[F, A, Labels, Repr](generic, () => summonAll[Map[Repr, F]])

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Sum.Labelled[A, Labels, Repr])
  : Generic.Sum.Instances.Labelled[F, A, Labels, Repr] =
    new Generic.Sum.Instances.Labelled[F, A, Labels, Repr](generic, () => summonAll[Map[Repr, F]])
end Generic
