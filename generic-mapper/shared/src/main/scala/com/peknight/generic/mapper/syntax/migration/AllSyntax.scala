package com.peknight.generic.mapper.syntax.migration

import cats.{Contravariant, FlatMap, Functor}
import com.peknight.generic.mapper.MigrationT

trait AllSyntax:
  extension [A] (a: A)
    def migrateTo[F[_], B](using migration: MigrationT[F, A, B]): F[B] = migration.migrate(a)
  end extension

  extension[F[_], A] (fa: F[A])(using functor: Functor[F])
    def mapTo[G[_], B](using migration: MigrationT[G, A, B]): F[G[B]] = functor.map(fa)(a => migration.migrate(a))
  end extension

  extension[F[_], A] (fa: F[A])(using flatMap: FlatMap[F])
    def flatMapTo[B](using migration: MigrationT[F, A, B]): F[B] = flatMap.flatMap(fa)(a => migration.migrate(a))
  end extension

  extension[F[_], G[_], A] (fga: F[G[A]])(using contravariant: Contravariant[F])
    def contramapTo[B](using migration: MigrationT[G, B, A]): F[B] = contravariant.contramap(fga)(b => migration.migrate(b))
  end extension
end AllSyntax

object AllSyntax extends AllSyntax
