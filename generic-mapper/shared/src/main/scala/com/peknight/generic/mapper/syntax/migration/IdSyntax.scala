package com.peknight.generic.mapper.syntax.migration

import cats.{Contravariant, Functor}
import com.peknight.generic.mapper.Migration

trait IdSyntax:
  extension [A] (a: A)
    def migrateTo[B](using migration: Migration[A, B]): B = migration.migrate(a)
  end extension

  extension [F[_], A] (fa: F[A])(using functor: Functor[F])
    def mapTo[B](using migration: Migration[A, B]): F[B] = functor.map(fa)(a => migration.migrate(a))
  end extension

  extension[F[_], A] (fa: F[A])(using contravariant: Contravariant[F])
    def contramapTo[B](using migration: Migration[B, A]): F[B] = contravariant.contramap(fa)(b => migration.migrate(b))
  end extension
end IdSyntax

object IdSyntax extends IdSyntax
