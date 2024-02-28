package com.peknight.generic.instances.squants.market

import cats.Applicative
import cats.syntax.applicative.*
import com.peknight.generic.migration.Isomorphism
import squants.market.{Money, MoneyContext}

trait MoneyInstances:
  given [F[_]](using applicative: Applicative[F], context: MoneyContext): Isomorphism[F, Money, BigDecimal] with
    def to(a: Money): F[BigDecimal] = a.to(context.defaultCurrency).pure[F]
    def from(b: BigDecimal): F[Money] = context.defaultCurrency(b).pure[F]
  end given
end MoneyInstances
object MoneyInstances extends MoneyInstances

