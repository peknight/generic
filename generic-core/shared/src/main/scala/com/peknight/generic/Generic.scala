package com.peknight.generic

import cats.Applicative
import com.peknight.generic.defaults.Default
import com.peknight.generic.tuple.Map
import com.peknight.generic.tuple.syntax.{foldLeft, foldRight, mapN}

import scala.Tuple.Size
import scala.compiletime.{constValue, constValueTuple, summonAll}

sealed trait Generic[A]:
  type Labels <: Tuple
  type Repr <: Tuple
  def size: Int
  def label: String
  def labels: Labels
  def defaults: Map[Repr, Option]
  def isProduct: Boolean
  def isSum: Boolean
end Generic

object Generic:
  def apply[A](using generic: Generic[A]): Generic[A] = generic

  type Aux[A, Repr0 <: Tuple] = Generic[A] { type Repr = Repr0 }

  sealed abstract class Labelled[A, Labels0 <: Tuple, Repr0 <: Tuple](
    val size: Int, val label: String, val labels: Labels0, val defaults: Map[Repr0, Option]
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
    type This[G[_]] <: Instances[G, A]
    type GenericType <: Generic.Labelled[A, Labels, Repr]
    def generic: GenericType
    def instances: Map[Repr, F]
    def mapK[G[_]](f: [T] => F[T] => G[T]): This[G]
    def size: Int = generic.size
    def label: String = generic.label
    def labels: Labels = generic.labels
    def defaults: Map[Repr, Option] = generic.defaults
    def isProduct: Boolean = generic.isProduct
    def isSum: Boolean = generic.isSum
    def derive(f: => Generic.Product.Instances[F, A] ?=> F[A], g: => Generic.Sum.Instances[F, A] ?=> F[A]): F[A] =
      if isProduct then f(using this.asInstanceOf) else g(using this.asInstanceOf)
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
    def isProduct: Boolean = true
    def isSum: Boolean = false
  end Product

  object Product:
    def apply[A](using generic: Generic.Product[A]): Generic.Product[A] = generic

    type Aux[A, Repr0 <: Tuple] = Generic.Product[A] { type Repr = Repr0 }

    sealed abstract class Labelled[A, Labels <: Tuple, Repr <: Tuple](override val size: Int,
      override val label: String, override val labels: Labels, override val defaults: Map[Repr, Option]
    ) extends Generic.Labelled[A, Labels, Repr](size, label, labels, defaults) with Generic.Product[A]

    final class MirrorLabelled[A <: scala.Product, Labels <: Tuple, Repr <: Tuple](
      mirror: Mirror.Product.Labelled[A, Labels, Repr],
      override val size: Int,
      override val label: String,
      override val labels: Labels,
      override val defaults: Map[Repr, Option],
    ) extends Generic.Product.Labelled[A, Labels, Repr](size, label, labels, defaults):
      def to(a: A): Repr = Tuple.fromProduct(a).asInstanceOf[Repr]
      def from(repr: Repr): A = mirror.fromProduct(repr)
    end MirrorLabelled

    object Labelled:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Product.Labelled[A, Labels, Repr])
      : Generic.Product.Labelled[A, Labels, Repr] = generic
    end Labelled

    sealed trait Instances[F[_], A] extends Generic.Instances[F, A]:
      type This[G[_]] = Generic.Product.Instances[G, A]
      type GenericType = Generic.Product.Labelled[A, Labels, Repr]
      def mapK[G[_]](f: [T] => F[T] => G[T]): Generic.Product.Instances[G, A]
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
      def construct[G[_]: Applicative](f: [T] => F[T] => G[T]): G[A] =
        instances.map[[_] =>> Any]([E] => (ft: E) => f.asInstanceOf[Any => Any](ft.asInstanceOf[Any]))
          .asInstanceOf[Map[Repr, G]]
          .mapN(from)

      def constructWithLabel[G[_]: Applicative](f: [T] => (F[T], String) => G[T]): G[A] =
        instances.zip(labels)
          .map[[_] =>> Any]([E] => (e: E) => f.asInstanceOf[(Any, Any) => Any].tupled(e.asInstanceOf[(Any, Any)]))
          .asInstanceOf[Map[Repr, G]]
          .mapN(from)

      def constructWithDefault[G[_]: Applicative](f: [T] => (F[T], Option[T]) => G[T]): G[A] =
        instances.zip(defaults)
          .map[[_] =>> Any]([E] => (e: E) => f.asInstanceOf[(Any, Any) => Any].tupled(e.asInstanceOf[(Any, Any)]))
          .asInstanceOf[Map[Repr, G]]
          .mapN(from)

      def constructWithLabelDefault[G[_]: Applicative](f: [T] => (F[T], String, Option[T]) => G[T]): G[A] =
        instances.zip(labels).zip(defaults)
          .map[[_] =>> Any] { [E] => (e: E) =>
            val ((instance, label), defaultOpt) = e.asInstanceOf[((Any, Any), Any)]
            f.asInstanceOf[(Any, Any, Any) => Any](instance, label, defaultOpt)
          }
          .asInstanceOf[Map[Repr, G]]
          .mapN(from)

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

      def foldRight[B](a: A)(b: B)(f: [T] => (F[T], T, B) => B): B =
        instances.zip(to(a)).foldRight[B](b) {
          [E] => (e: E, b: B) =>
            type U = E match {case (_, t) => t}
            val (instance, value) = e.asInstanceOf[(F[U], U)]
            f(instance, value, b)
        }

      def foldRightWithLabel[B](a: A)(b: B)(f: [T] => (F[T], T, String, B) => B): B =
        instances.zip(to(a)).zip(labels).foldRight[B](b) {
          [E] => (e: E, b: B) =>
            type U = E match {case ((_, t), _) => t}
            val ((instance, value), label) = e.asInstanceOf[((F[U], U), String)]
            f(instance, value, label, b)
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
      ) with Generic.Product.Instances[F, A]:
        lazy val is = instances0()
        def mapK[G[_]](f: [T] => F[T] => G[T]): Generic.Product.Instances[G, A] =
          new Generic.Product.Instances.Labelled(generic, () =>
            is.map[[_] =>> Any]([T] => (t: T) => f.asInstanceOf[Any => Any](t)).asInstanceOf[Map[Repr, G]]
          )
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
    def isProduct: Boolean = false
    def isSum: Boolean = true
  end Sum

  object Sum:
    def apply[A](using generic: Generic.Sum[A]): Generic.Sum[A] = generic

    type Aux[A, Repr0 <: Tuple] = Generic.Sum[A] { type Repr = Repr0 }

    sealed abstract class Labelled[A, Labels <: Tuple, Repr <: Tuple](
      override val size: Int,
      override val label: String,
      override val labels: Labels,
      override val defaults: Map[Repr, Option]
    ) extends Generic.Labelled[A, Labels, Repr](
      size, label, labels, defaults
    ) with Generic.Sum[A]

    final class MirrorLabelled[A, Labels <: Tuple, Repr <: Tuple](
      mirror: Mirror.Sum.Labelled[A, Labels, Repr],
      override val size: Int,
      override val label: String,
      override val labels: Labels,
      override val defaults: Map[Repr, Option]
    ) extends Generic.Sum.Labelled[A, Labels, Repr](size, label, labels, defaults):
      def ordinal(a: A): Int = mirror.ordinal(a)
    end MirrorLabelled

    object Labelled:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Sum.Labelled[A, Labels, Repr])
      : Generic.Sum.Labelled[A, Labels, Repr] = generic
    end Labelled

    sealed trait Instances[F[_], A] extends Generic.Instances[F, A]:
      type This[G[_]] = Generic.Sum.Instances[G, A]
      type GenericType = Generic.Sum.Labelled[A, Labels, Repr]
      def mapK[G[_]](f: [T] => F[T] => G[T]): Generic.Sum.Instances[G, A]
      def instance(ord: Int): F[A] = instances.productElement(ord).asInstanceOf[F[A]]
      def instance(a: A): F[A] = instance(ordinal(a))

      def withInstance[B](a: A)(f: F[A] => B): B = f(instance(a))

      def withLabel[B](a: A)(f: (F[A], String) => B): B =
        val ord = ordinal(a)
        f(instance(ord), label(ord))

      def ordinal(a: A): Int = generic.ordinal(a)
      def label(ord: Int): String = generic.label(ord)
      def label(a: A): String = generic.label(a)

      def foldLeft[B](b: B)(f: [T] => (B, F[T]) => B): B =
        instances.foldLeft[B](b)([E] => (b: B, e: E) => f.asInstanceOf[(B, Any) => B](b, e))
      def foldLeftWithLabel[B](b: B)(f: [T] => (B, F[T], String) => B): B =
        instances.zip(labels).foldLeft[B](b) {
          [E] => (b: B, e: E) =>
            val (instance, label) = e.asInstanceOf[(Any, String)]
            f.asInstanceOf[(B, Any, String) => B](b, instance, label)
        }

      def foldRight[B](b: B)(f: [T] => (F[T], B) => B): B =
        instances.foldRight[B](b)([E] => (e: E, b: B) => f.asInstanceOf[(Any, B) => B](e, b))

      def foldRightWithLabel[B](b: B)(f: [T] => (F[T], String, B) => B): B =
        instances.zip(labels).foldRight[B](b) {
          [E] => (e: E, b: B) =>
            type U = E match {case ((_, t), _) => t}
            val (instance, label) = e.asInstanceOf[(Any, String)]
            f.asInstanceOf[(Any, String, B) => B](instance, label, b)
        }
    end Instances

    object Instances:
      def apply[F[_], A](using inst: Generic.Sum.Instances[F, A]): Generic.Sum.Instances[F, A] = inst

      type Aux[F[_], A, Repr0 <: Tuple] = Generic.Sum.Instances[F, A] { type Repr = Repr0 }

      final class Labelled[F[_], A, Labels <: Tuple, Repr <: Tuple](
        override val generic: Generic.Sum.Labelled[A, Labels, Repr],
        instances0: () => Map[Repr, F]
      ) extends Generic.Instances.Labelled[F, A, Labels, Repr, Generic.Sum.Labelled[A, Labels, Repr]](
        generic, instances0
      ) with Generic.Sum.Instances[F, A]:
        lazy val is = instances0()
        def mapK[G[_]](f: [T] => F[T] => G[T]): Generic.Sum.Instances[G, A] =
          new Generic.Sum.Instances.Labelled(generic, () =>
            is.map[[_] =>> Any]([T] => (t: T) => f.asInstanceOf[Any => Any](t)).asInstanceOf[Map[Repr, G]]
          )
      object Labelled:
        def apply[F[_], A, Labels <: Tuple, Repr <: Tuple](using inst: Generic.Sum.Instances.Labelled[F, A, Labels, Repr])
        : Generic.Sum.Instances.Labelled[F, A, Labels, Repr] = inst
      end Labelled
    end Instances
  end Sum

  inline given [A <: scala.Product, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Product.Labelled[A, Labels, Repr]
  ): Generic.Product.MirrorLabelled[A, Labels, Repr] =
    val size = constValue[Size[Repr]]
    new Generic.Product.MirrorLabelled[A, Labels, Repr](mirror, size, constValue[mirror.MirroredLabel],
      constValueTuple[Labels], Default.getDefaults[A](size).asInstanceOf[Map[Repr, Option]])

  inline given [A, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Sum.Labelled[A, Labels, Repr]
  ): Generic.Sum.MirrorLabelled[A, Labels, Repr] =
    val size = constValue[Size[Repr]]
    new Generic.Sum.MirrorLabelled[A, Labels, Repr](mirror, size, constValue[mirror.MirroredLabel],
      constValueTuple[Labels], Default.getDefaults[A](size).asInstanceOf[Map[Repr, Option]])

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Product.Labelled[A, Labels, Repr])
  : Generic.Product.Instances.Labelled[F, A, Labels, Repr] =
    new Generic.Product.Instances.Labelled[F, A, Labels, Repr](generic, () => summonAll[Map[Repr, F]])

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](using generic: Generic.Sum.Labelled[A, Labels, Repr])
  : Generic.Sum.Instances.Labelled[F, A, Labels, Repr] =
    new Generic.Sum.Instances.Labelled[F, A, Labels, Repr](generic, () => summonAll[Map[Repr, F]])
end Generic
