package com.peknight.generic.migration

import cats.Applicative
import cats.data.Kleisli
import cats.syntax.applicative.*
import com.peknight.generic.Mirror
import com.peknight.generic.migration.instances.MigrationInstances
import com.peknight.generic.migration.{Isomorphism, Migration}
import com.peknight.generic.tuple.Second
import com.peknight.generic.tuple.ops.{Align, Intersection}

import scala.Tuple.Zip
import scala.compiletime.{constValue, constValueTuple}

trait Migration[F[_], -A, B]:
  def migrate(a: A): F[B]
  def kleisli: Kleisli[F, A, B] = Kleisli(migrate)
end Migration
object Migration extends MigrationInstances:
  def apply[F[_], A, B](f: A => F[B]): Migration[F, A, B] = f(_)

  given [F[_], A, B](using iso: Isomorphism[F, A, B]): Migration[F, A, B] with
    def migrate(a: A): F[B] = iso.to(a)
  end given

  given [F[_], A <: Product, Repr <: Tuple](using applicative: Applicative[F], mirror: Mirror.Product.Aux[A, Repr])
  : Migration[F, A, Repr] with
    def migrate(a: A): F[Repr] = Tuple.fromProductTyped(a).pure[F]
  end given

  given [F[_], A, Repr <: Tuple](using applicative: Applicative[F], mirror: Mirror.Product.Aux[A, Repr])
  : Migration[F, Repr, A] with
    def migrate(a: Repr): F[A] = mirror.fromProduct(a).pure[F]
  end given

  //noinspection ConvertExpressionToSAM
  inline given [F[_], A <: Product, ALabels <: Tuple, ARepr <: Tuple, B, BLabels <: Tuple, BRepr <: Tuple,
    Common <: Tuple, Unaligned <: Tuple](
    using
    applicative: Applicative[F],
    aMirror: Mirror.Product.Labelled[A, ALabels, ARepr],
    bMirror: Mirror.Product.Labelled[B, BLabels, BRepr],
    inter: Intersection.Aux[Zip[ALabels, ARepr], Zip[BLabels, BRepr], Common],
    align: Align[Common, Zip[BLabels, BRepr]]
  ): Migration[F, A, B] =
    new Migration[F, A, B]:
      def migrate(a: A): F[B] =
        bMirror.fromProduct(align(inter(constValueTuple[ALabels].zip(Tuple.fromProductTyped(a))))
          .map[Second] {
            [T] => (t: T) => t match
              case (_, value) => value.asInstanceOf[Second[T]]
          }
        ).pure[F]
  end given
end Migration
