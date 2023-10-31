package com.peknight.generic.circe

import io.circe.Encoder

trait ConfiguredSumEncoder[A] extends Encoder.AsObject[A]
