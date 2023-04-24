package com.peknight.generic.mapper

import cats.data.Kleisli
import com.peknight.generic.mapper.instances.selector.AllInstances

trait SelectorT[F[_], -A, B]:
  def select(a: A): F[B]
  def kleisli: Kleisli[F, A, B] = Kleisli(select)
end SelectorT
object SelectorT extends AllInstances:
  def apply[F[_], A, B](f: A => F[B]): SelectorT[F, A, B] = f(_)
end SelectorT
