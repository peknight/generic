package io.circe

object DecoderOps:
  def isKeyMissingNone[A](r: Decoder.Result[A]): Boolean = r.eq(Decoder.keyMissingNone)
  def isKeyMissingNoneAccumulating[A](r: Decoder.AccumulatingResult[A]): Boolean = r.eq(Decoder.keyMissingNoneAccumulating)
end DecoderOps
