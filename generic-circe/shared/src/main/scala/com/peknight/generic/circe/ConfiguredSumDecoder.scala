package com.peknight.generic.circe

import com.peknight.generic.deriving.Generic
import io.circe.Decoder
import io.circe.derivation.Configuration

trait ConfiguredSumDecoder[A] extends Decoder[A]:
  def configuration: Configuration
  def decoders: Generic.Sum.Instances[Decoder, A]
end ConfiguredSumDecoder

