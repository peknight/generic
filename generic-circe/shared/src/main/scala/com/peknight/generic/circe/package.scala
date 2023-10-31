package com.peknight.generic

package object circe:
  object all extends EncoderInstances with DecoderInstances
  object encoder extends EncoderInstances
  object decoder extends DecoderInstances
end circe
