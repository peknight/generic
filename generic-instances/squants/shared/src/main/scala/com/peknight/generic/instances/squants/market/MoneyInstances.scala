package com.peknight.generic.instances.squants.market

import cats.Id
import com.peknight.generic.mapper.Bidirectional
import squants.market.{Money, MoneyContext}

trait MoneyInstances:
  given (using context: MoneyContext): Bidirectional[Money, BigDecimal] with
    def to(a: Money): Id[BigDecimal] = a.to(context.defaultCurrency)
    def from(b: BigDecimal): Id[Money] = context.defaultCurrency(b)
  end given
end MoneyInstances
object MoneyInstances extends MoneyInstances

