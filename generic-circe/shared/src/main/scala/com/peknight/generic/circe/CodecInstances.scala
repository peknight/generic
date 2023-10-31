package com.peknight.generic.circe

import io.circe.derivation.Configuration
import com.peknight.generic.deriving.Generic
import io.circe.Decoder.{AccumulatingResult, Result}
import io.circe.{Codec, Decoder, Encoder, HCursor, JsonObject}

object CodecInstances:

  inline final def derivedConfiguredCodec[A](configuration: Configuration = Configuration.default)
                                            (using generic: Generic[A],
                                             encoders: => Generic.Instances[Encoder, A],
                                             decoders: => Generic.Instances[Decoder, A]): Codec.AsObject[A] =
    if generic.isProduct then derivedProduct(configuration, encoders.asInstanceOf, decoders.asInstanceOf)
    else derivedSum(configuration, encoders.asInstanceOf, decoders.asInstanceOf)

  private[this] def derivedProduct[A](configuration: Configuration,
                                      encoders: => Generic.Product.Instances[Encoder, A],
                                      decoders: => Generic.Product.Instances[Decoder, A]): Codec.AsObject[A] =
    new Codec.AsObject[A]:
      def encodeObject(a: A): JsonObject = EncoderInstances.encodeProduct(a, configuration, encoders)
      def apply(c: HCursor): Result[A] = DecoderInstances.decodeProductResult(c, configuration, decoders)
      override def decodeAccumulating(c: HCursor): AccumulatingResult[A] =
        DecoderInstances.decodeProductAccumulating(c, configuration, decoders)
  end derivedProduct

  private[this] def derivedSum[A](configuration0: Configuration,
                                  encoders: => Generic.Sum.Instances[Encoder, A],
                                  decoders0: => Generic.Sum.Instances[Decoder, A]): Codec.AsObject[A] =
    new Codec.AsObject[A] with ConfiguredSumEncoder[A] with ConfiguredSumDecoder[A]:
      def configuration: Configuration = configuration0
      def decoders: Generic.Sum.Instances[Decoder, A] = decoders0
      def encodeObject(a: A): JsonObject = EncoderInstances.encodeSum(a, configuration, encoders)
      def apply(c: HCursor): Result[A] = DecoderInstances.decodeSumResult(c, configuration, decoders)
      override def decodeAccumulating(c: HCursor): AccumulatingResult[A] =
        DecoderInstances.decodeSumAccumulating(c, configuration, decoders)
  end derivedSum
end CodecInstances
