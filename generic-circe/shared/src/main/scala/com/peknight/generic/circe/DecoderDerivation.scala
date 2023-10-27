package com.peknight.generic.circe

import com.peknight.generic.deriving.Generic
import io.circe.DecodingFailure.Reason.WrongTypeExpectation
import io.circe.derivation.{Configuration, Default}
import io.circe.{ACursor, Decoder, DecoderOps, DecodingFailure, HCursor}

trait DecoderDerivation:
  inline final def derivedConfigured[A](using instances: => Generic.Instances[Decoder, A], defaults: => Default[A],
                                        configuration: Configuration): Decoder[A] =
    instances.derive(
      inst ?=> derivedConfiguredProduct[A](inst, defaults, configuration),
      inst ?=> derivedConfiguredSum[A](inst, defaults, configuration)
    )

  private[this] inline final def derivedConfiguredProduct[A](instances: => Generic.Product.Instances[Decoder, A],
                                                             defaults: => Default[A],
                                                             configuration: Configuration): Decoder[A] =
    ???


  private[this] inline final def derivedConfiguredSum[A](instances: => Generic.Sum.Instances[Decoder, A],
                                                         defaults: => Default[A],
                                                         configuration: Configuration): Decoder[A] =
    ???

  private[this] def strictDecodingFailure(c: HCursor, name: String, message: String): DecodingFailure =
    DecodingFailure(s"Strict decoding $name - $message", c.history)

  private[this] def decodeProductElement[A, R](c: HCursor): R =

    ???

  private[this] def decodeProduct[A](c: HCursor, instances: => Generic.Product.Instances[Decoder, A],
                                     configuration: Configuration): Decoder.Result[A] =
    def strictFail(unexpectedFields: List[String], expectedFields: IndexedSeq[String]): Decoder.Result[A] =
      Left(strictDecodingFailure(c, instances.label,
        s"unexpected fields: ${unexpectedFields.mkString(", ")}; valid fields: ${expectedFields.mkString(", ")}."
      ))
    decodeProductBase[A, Decoder.Result[A]](c, Left.apply, strictFail,
      instances.labels.toList.asInstanceOf[List[String]], configuration.strictDecoding, configuration.discriminator) {
      def withDefault(result: Decoder.Result[Any], cursor: ACursor, default: Any): Decoder.Result[Any] = result match
        case r @ Right(_) if !DecoderOps.isKeyMissingNone(r) => r
        case l @ Left(_) if cursor.succeeded && !cursor.focus.exists(_.isNull) => l
        case _ => Right(default)
      end withDefault
      ???
    }

  private[this] def decodeProductBase[A, R](
                                             c: HCursor,
                                             fail: DecodingFailure => R,
                                             strictFail: (List[String], IndexedSeq[String]) => R,
                                             elemLabels: List[String],
                                             strictDecoding: Boolean,
                                             discriminator: Option[String]
                                           )(decodeProduct: => R): R =
    c.value.isObject match
      case false => fail(DecodingFailure(WrongTypeExpectation("object", c.value), c.history))
      case true if !strictDecoding => decodeProduct
      case true =>
        val expectedFields = elemLabels.toIndexedSeq ++ discriminator
        val expectedFieldsSet = expectedFields.toSet
        val unexpectedFields = c.keys.map(_.toList.filterNot(expectedFieldsSet)).getOrElse(Nil)
        if unexpectedFields.nonEmpty then strictFail(unexpectedFields, expectedFields)
        else decodeProduct
  end decodeProductBase
end DecoderDerivation
object DecoderDerivation extends DecoderDerivation:
  case class A(a: String, b: Boolean, c: Int, d: String = "rua", e: Boolean = true, f: Int = 600)
  def main(args: Array[String]): Unit = {
    println(summon[Default[A]].defaults)
  }
