package com.peknight.generic.mapper

import com.peknight.generic.mapper.instances.bidirectional.AllInstances

case class BidirectionalT[F[_], A, B](to: A => F[B], from: B => F[A])

object BidirectionalT extends AllInstances