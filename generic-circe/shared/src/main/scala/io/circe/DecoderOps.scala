package io.circe

object DecoderOps:
  def isKeyMissingNone[A](r: Decoder.Result[A]): Boolean = r.eq(Decoder.keyMissingNone)
end DecoderOps
