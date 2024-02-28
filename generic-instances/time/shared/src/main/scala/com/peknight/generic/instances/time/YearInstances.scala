package com.peknight.generic.instances.time

import cats.Applicative
import cats.syntax.applicative.*
import com.peknight.generic.migration.Isomorphism

import java.time.Year

trait YearInstances:
  given [F[_]: Applicative]: Isomorphism[F, Year, Int] with
    def to(a: Year): F[Int] = a.getValue.pure[F]
    def from(b: Int): F[Year] = Year.of(b).pure[F]
  end given
end YearInstances
object YearInstances extends YearInstances
