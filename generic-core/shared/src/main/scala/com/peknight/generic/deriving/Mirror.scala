package com.peknight.generic.deriving

import com.peknight.generic.compiletime.summonValuesAsTuple

import scala.deriving.Mirror as SMirror

object Mirror:

  def apply[A](using mirror: Mirror[A]): Mirror[A] = mirror
  inline def labels[A](using mirror: Mirror[A]): mirror.MirroredElemLabels =
    summonValuesAsTuple[mirror.MirroredElemLabels]

  type Aux[A, Repr <: Tuple] = Mirror[A] { type MirroredElemTypes = Repr }

  object Aux:
    def apply[A, Repr <: Tuple](using mirror: Aux[A, Repr]): Aux[A, Repr] = mirror
  end Aux


  type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Mirror[A] {
    type MirroredElemLabels = Labels
    type MirroredElemTypes = Repr
  }

  object Labelled:
    def apply[A, Labels <: Tuple, Repr <: Tuple](using mirror: Labelled[A, Labels, Repr]): Labelled[A, Labels, Repr] =
      mirror
  end Labelled


  type Product[A] = SMirror.ProductOf[A]

  object Product:
    def apply[A](using mirror: Product[A]): Product[A] = mirror

    def fromProduct[A](product: scala.Product)(using mirror: Product[A]): mirror.MirroredMonoType =
      mirror.fromProduct(product)

    type Aux[A, Repr <: Tuple] = Product[A] { type MirroredElemTypes = Repr }

    object Aux:
      def apply[A, Repr <: Tuple](using mirror: Aux[A, Repr]): Aux[A, Repr] = mirror
    end Aux

    type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Product[A] {
      type MirroredElemLabels = Labels
      type MirroredElemTypes = Repr
    }

    object Labelled:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using mirror: Labelled[A, Labels, Repr]): Labelled[A, Labels, Repr] =
        mirror
    end Labelled
  end Product


  type Sum[A] = SMirror.SumOf[A]

  object Sum:
    def apply[A](using mirror: Sum[A]): Sum[A] = mirror

    def ordinal[A](a: A)(using mirror: Sum[A]): Int = mirror.ordinal(a)

    inline def label[A](a: A)(using mirror: Sum[A]): String =
      summonValuesAsTuple[mirror.MirroredElemLabels].productElement(mirror.ordinal(a)).asInstanceOf[String]

    type Aux[A, Repr <: Tuple] = Sum[A] { type MirroredElemTypes = Repr }

    object Aux:
      def apply[A, Repr <: Tuple](using mirror: Aux[A, Repr]): Aux[A, Repr] = mirror
    end Aux

    type Labelled[A, Labels <: Tuple, Repr <: Tuple] = Sum[A] {
      type MirroredElemLabels = Labels
      type MirroredElemTypes = Repr
    }

    object Labelled:
      def apply[A, Labels <: Tuple, Repr <: Tuple](using mirror: Labelled[A, Labels, Repr]): Labelled[A, Labels, Repr] =
        mirror
    end Labelled
  end Sum
end Mirror
