package com.peknight.generic.instances.time

import cats.Id
import com.peknight.generic.migration.id.Isomorphism

import java.time.Year

trait YearInstances:
  given Isomorphism[Year, Int] with
    def to(a: Year): Id[Int] = a.getValue
    def from(b: Int): Id[Year] = Year.of(b)
  end given
end YearInstances
object YearInstances extends YearInstances
