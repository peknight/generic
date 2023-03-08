package com.peknight.generic.deriving

import scala.compiletime.constValueTuple
import scala.deriving.Mirror as SMirror

object Mirror:

  def apply[A](using mirror: Mirror[A]): Mirror[A] = mirror
  inline def labels[A](using mirror: Mirror[A]): mirror.MirroredElemLabels =
    constValueTuple[mirror.MirroredElemLabels]

  type Aux[A, Repr <: Tuple] = Mirror[A] { type MirroredElemTypes = Repr }

  object Aux:
    def apply[A, Repr <: Tuple](using mirror: Mirror.Aux[A, Repr]): Mirror.Aux[A, Repr] = mirror
  end Aux


  type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Mirror[A] {
    type MirroredElemLabels = Labels
    type MirroredElemTypes = Repr
  }

  object Labelled:
    def apply[A, Labels <: Tuple, Repr <: Tuple](using mirror: Mirror.Labelled[A, Labels, Repr])
    : Mirror.Labelled[A, Labels, Repr] = mirror
  end Labelled


  type Product[A] = SMirror.ProductOf[A]

  object Product:
    def apply[A](using mirror: Mirror.Product[A]): Mirror.Product[A] = mirror

    def fromProduct[A](product: scala.Product)(using mirror: Mirror.Product[A]): mirror.MirroredMonoType =
      mirror.fromProduct(product)

    type Aux[A, Repr <: Tuple] = Mirror.Product[A] { type MirroredElemTypes = Repr }

    object Aux:
      def apply[A, Repr <: Tuple](using mirror: Mirror.Product.Aux[A, Repr]): Aux[A, Repr] = mirror
    end Aux

    type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Mirror.Product[A] {
      type MirroredElemLabels = Labels
      type MirroredElemTypes = Repr
    }

    object Labelled:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using mirror: Mirror.Product.Labelled[A, Labels, Repr])
      : Mirror.Product.Labelled[A, Labels, Repr] = mirror
    end Labelled
  end Product


  type Sum[A] = SMirror.SumOf[A]

  object Sum:
    def apply[A](using mirror: Mirror.Sum[A]): Mirror.Sum[A] = mirror

    def ordinal[A](a: A)(using mirror: Mirror.Sum[A]): Int = mirror.ordinal(a)

    inline def label[A](a: A)(using mirror: Mirror.Sum[A]): String =
      constValueTuple[mirror.MirroredElemLabels].productElement(mirror.ordinal(a)).asInstanceOf[String]

    type Aux[A, Repr <: Tuple] = Mirror.Sum[A] { type MirroredElemTypes = Repr }

    object Aux:
      def apply[A, Repr <: Tuple](using mirror: Mirror.Sum.Aux[A, Repr]): Mirror.Sum.Aux[A, Repr] = mirror
    end Aux

    type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Mirror.Sum[A] {
      type MirroredElemLabels = Labels
      type MirroredElemTypes = Repr
    }

    object Labelled:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using mirror: Mirror.Sum.Labelled[A, Labels, Repr])
      : Mirror.Sum.Labelled[A, Labels, Repr] = mirror
    end Labelled
  end Sum
end Mirror
