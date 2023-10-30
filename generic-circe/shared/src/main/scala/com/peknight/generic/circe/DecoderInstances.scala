package com.peknight.generic.circe

import cats.Applicative
import cats.data.{NonEmptyList, Validated}
import com.peknight.generic.deriving.Generic
import io.circe.*
import io.circe.Decoder.{AccumulatingResult, Result}
import io.circe.DecodingFailure.Reason.WrongTypeExpectation
import io.circe.`export`.Exported
import io.circe.derivation.Configuration

trait DecoderInstances:

  trait ConfiguredDecoder[A] extends Decoder[A]:
    def configuration: Configuration
    def instances: Generic.Instances[Decoder, A]
  end ConfiguredDecoder

  inline given derived[A](using configuration: Configuration, instances: => Generic.Instances[Decoder, A])
  : Exported[Decoder[A]] =
    Exported(derivedConfigured(configuration)(using instances))

  inline final def derivedConfigured[A](configuration: Configuration = Configuration.default)
                                       (using instances: => Generic.Instances[Decoder, A]): Decoder[A] =
    instances.derive(
      inst ?=> derivedConfiguredProduct[A](configuration, inst),
      inst ?=> derivedConfiguredSum[A](configuration, inst)
    )

  private[this] def strictDecodingFailure(c: HCursor, name: String, message: String): DecodingFailure =
    DecodingFailure(s"Strict decoding $name - $message", c.history)
  end strictDecodingFailure

  private[this] inline final def derivedConfiguredProduct[A](configuration0: Configuration,
                                                             instances0: => Generic.Product.Instances[Decoder, A])
  : Decoder[A] =
    new ConfiguredDecoder[A]:
      def configuration: Configuration = configuration0
      def instances: Generic.Instances[Decoder, A] = instances0
      def apply(c: HCursor): Result[A] =
        decodeProduct[Result, A](
          c,
          Left.apply,
          (unexpectedFields, expectedFields) => Left(strictDecodingFailure(c, instances.label,
            s"unexpected fields: ${unexpectedFields.mkString(", ")}; valid fields: ${expectedFields.mkString(", ")}."
          )),
          [T] => (decoder: Decoder[T]) => decoder.tryDecode,
          [T] => (result: Result[T]) => result.isRight,
          [T] => (result: Result[T]) => DecoderOps.isKeyMissingNone(result),
          configuration0,
          instances0
        )
      override def decodeAccumulating(c: HCursor): AccumulatingResult[A] =
        decodeProduct[AccumulatingResult, A](
          c,
          Validated.invalidNel,
          (unexpectedFields, expectedFields) => Validated.invalid(NonEmptyList.fromListUnsafe(unexpectedFields
            .map(field => strictDecodingFailure(c, instances.label,
              s"unexpected field: $field; valid fields: ${expectedFields.mkString(", ")}."
            )))),
          [T] => (decoder: Decoder[T]) => decoder.tryDecodeAccumulating,
          [T] => (result: AccumulatingResult[T]) => result.isValid,
          [T] => (result: AccumulatingResult[T]) => DecoderOps.isKeyMissingNoneAccumulating(result),
          configuration0,
          instances0
        )
  end derivedConfiguredProduct

  private[this] inline final def derivedConfiguredSum[A](configuration0: Configuration,
                                                         instances0: => Generic.Sum.Instances[Decoder, A])
  : Decoder[A] =
    new ConfiguredDecoder[A]:
      def configuration: Configuration = configuration0
      def instances: Generic.Instances[Decoder, A] = instances0
      def apply(c: HCursor): Result[A] =
        decodeSum[Result, A](c, Left.apply, _.tryDecode, configuration0, instances0)
      override def decodeAccumulating(c: HCursor): AccumulatingResult[A] =
        decodeSum[AccumulatingResult, A](c, Validated.invalidNel, _.tryDecodeAccumulating, configuration0, instances0)
  end derivedConfiguredSum


  private[this] def decodeProduct[F[_]: Applicative, A](
                                                         c: HCursor,
                                                         fail: DecodingFailure => F[A],
                                                         strictFail: (List[String], IndexedSeq[String]) => F[A],
                                                         decode: [T] => Decoder[T] => ACursor => F[T],
                                                         isFailed: [T] => F[T] => Boolean,
                                                         isKeyMissingNone: [T] => F[T] => Boolean,
                                                         configuration: Configuration,
                                                         instances: => Generic.Product.Instances[Decoder, A]
                                                      ): F[A] =
    def go: F[A] =
      instances.constructWithLabelDefault[F] { [T] => (decoder: Decoder[T], label: String, defaultOpt: Option[T]) =>
        val cursor: ACursor = c.downField(configuration.transformMemberNames(label))
        val result: F[T] = decode(decoder)(cursor)
        defaultOpt.fold(result) { defaultValue =>
          if isFailed(result) && !isKeyMissingNone(result) then result
          else if !isFailed(result) && cursor.succeeded && !cursor.focus.exists(_.isNull) then result
          else Applicative[F].pure(defaultValue)
        }
      }
    c.value.isObject match
      case false => fail(DecodingFailure(WrongTypeExpectation("object", c.value), c.history))
      case true if !configuration.strictDecoding => go
      case true =>
        val expectedFields = instances.labels.toList.asInstanceOf[List[String]].toIndexedSeq ++
          configuration.discriminator
        val expectedFieldsSet = expectedFields.toSet
        val unexpectedFields = c.keys.map(_.toList.filterNot(expectedFieldsSet)).getOrElse(Nil)
        if unexpectedFields.nonEmpty then strictFail(unexpectedFields, expectedFields)
        else go
  end decodeProduct

  private[this] def decodeSum[F[_]: Applicative, A](
                                                    c: HCursor,
                                                    fail: DecodingFailure => F[A],
                                                    decode: Decoder[A] => ACursor => F[A],
                                                    configuration: Configuration,
                                                    instances: => Generic.Sum.Instances[Decoder, A]
                                                   ): F[A] =
    def decodersDict(conf: Configuration, inst: => Generic.Sum.Instances[Decoder, A]): Map[String, Decoder[?]] =
      inst.foldRightWithLabel(Map.empty[String, Decoder[?]]) {
        [T] => (decoder: Decoder[T], label: String, map: Map[String, Decoder[?]]) =>
          decoder match
            case d: ConfiguredDecoder[?] if d.instances.isSum =>
              map ++ decodersDict(d.configuration, d.instances.asInstanceOf[Generic.Sum.Instances[Decoder, A]])
            case _ => map + (conf.transformConstructorNames(label) -> decoder)
      }
    def fromName(sumTypeName: String, cursor: ACursor): F[A] =
      decodersDict(configuration, instances).get(sumTypeName)
        .fold(fail(DecodingFailure(s"type ${instances.label} has no class/object/case '$sumTypeName'.", cursor.history))) {
          decoder => decode(decoder.asInstanceOf[Decoder[A]])(cursor)
        }
    configuration.discriminator match
      case Some(discriminator) =>
        val cursor = c.downField(discriminator)
        cursor.as[Option[String]] match
          case Left(failure) => fail(failure)
          case Right(None) => fail(DecodingFailure(
            s"${instances.label}: could not find discriminator field '$discriminator' or its null.'", cursor.history
          ))
          case Right(Some(sumTypeName)) => fromName(sumTypeName, c)
      case _ =>
        c.keys match
          case None => fail(DecodingFailure(WrongTypeExpectation("object", c.value), c.history))
          case Some(keys) =>
            val iter = keys.iterator
            if !iter.hasNext then
              fail(DecodingFailure(WrongTypeExpectation("non-empty json object", c.value), c.history))
            else
              val sumTypeName = iter.next
              if iter.hasNext && configuration.strictDecoding then
                val constructorNames = instances.labels.toList.asInstanceOf[List[String]]
                  .map(configuration.transformConstructorNames).mkString(", ")
                fail(strictDecodingFailure(c, instances.label,
                  s"expected a single key json object with one of: $constructorNames."
                ))
              else fromName(sumTypeName, c.downField(sumTypeName))
  end decodeSum

end DecoderInstances
object DecoderInstances extends DecoderInstances