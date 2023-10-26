package com.peknight.generic.circe

import com.peknight.generic.deriving.Generic
import io.circe.DecodingFailure.Reason.WrongTypeExpectation
import io.circe.derivation.Configuration
import io.circe.{Decoder, DecodingFailure, HCursor}

trait DecoderDerivation:
  inline final def derivedConfigured[A](using instances: => Generic.Instances[Decoder, A],
                                        configuration: Configuration): Decoder[A] =
    instances.derive(
      inst ?=> derivedConfiguredProduct[A](inst, configuration),
      inst ?=> derivedConfiguredSum[A](inst, configuration)
    )

  private[this] inline final def derivedConfiguredProduct[A](instances: => Generic.Product.Instances[Decoder, A],
                                                             configuration: Configuration): Decoder[A] =
    ???


  private[this] inline final def derivedConfiguredSum[A](instances: => Generic.Sum.Instances[Decoder, A],
                                                         configuration: Configuration): Decoder[A] =
    ???

  private[this] def decodeProductBase[A, R](
                                             c: HCursor,
                                             fail: DecodingFailure => R,
                                             strictFail: (List[String], IndexedSeq[String]) => R,
                                             instances: => Generic.Product.Instances[Decoder, A],
                                             configuration: Configuration
                                           )(decodeProduct: => R): R =
    c.value.isObject match
      case false => fail(DecodingFailure(WrongTypeExpectation("object", c.value), c.history))
      case true if !configuration.strictDecoding => decodeProduct
      case true =>
        val expectedFields = instances.labels.toList.asInstanceOf[List[String]] ++ configuration.discriminator
        // TODO
        decodeProduct
    ???
end DecoderDerivation
