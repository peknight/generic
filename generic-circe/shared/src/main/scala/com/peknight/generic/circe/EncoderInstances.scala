package com.peknight.generic.circe

import com.peknight.generic.deriving.Generic
import io.circe.`export`.Exported
import io.circe.derivation.Configuration
import io.circe.{Encoder, Json, JsonObject}

trait EncoderInstances:
  inline given derivedEncoder[A](using configuration: Configuration, instances: => Generic.Instances[Encoder, A])
  : Exported[Encoder[A]] =
    Exported(EncoderInstances.derived(configuration)(using instances))
end EncoderInstances
object EncoderInstances extends EncoderInstances:
  inline final def derived[A](configuration: Configuration = Configuration.default)
                             (using instances: => Generic.Instances[Encoder, A]): Encoder.AsObject[A] =
    inline instances match
      case p: Generic.Product.Instances[Encoder, A] => derivedProduct(configuration, p)
      case s: Generic.Sum.Instances[Encoder, A] => derivedSum(configuration, s)
  end derived

  private[this] final def derivedProduct[A](configuration: Configuration,
                                            instances: => Generic.Product.Instances[Encoder, A]): Encoder.AsObject[A] =
    new Encoder.AsObject[A]:
      def encodeObject(a: A): JsonObject = encodeProduct(a, configuration, instances)
  end derivedProduct

  private[this] final def derivedSum[A](configuration: Configuration,
                                        instances: => Generic.Sum.Instances[Encoder, A]): Encoder.AsObject[A] =
    new ConfiguredSumEncoder[A]:
      def encodeObject(a: A): JsonObject = encodeSum(a, configuration, instances)
  end derivedSum

  private[circe] def encodeProduct[A](a: A, configuration: Configuration,
                                      instances: => Generic.Product.Instances[Encoder, A]): JsonObject =
    JsonObject.fromFoldable[List](instances.foldRightWithLabel[List[(String, Json)]](a)(List.empty) {
      [T] => (instance: Encoder[T], t: T, label: String, acc: List[(String, Json)]) =>
        (configuration.transformMemberNames(label), instance(t)) :: acc
    })
  end encodeProduct

  private[circe] def encodeSum[A](a: A, configuration: Configuration,
                                  instances: => Generic.Sum.Instances[Encoder, A]): JsonObject =
    val constructorName = configuration.transformConstructorNames(instances.label(a))
    val encoder = instances.instance(a)
    val json = encoder(a)
    val jo = json.asObject.getOrElse(JsonObject.empty)
    val isSum = encoder match
      case e: ConfiguredSumEncoder[?] => true
      case _ => configuration.discriminator.exists(jo.contains)
    if isSum then jo
    else
      configuration.discriminator match
        case Some(discriminator) => jo.add(discriminator, Json.fromString(constructorName))
        case _ => JsonObject.singleton(constructorName, json)
  end encodeSum

end EncoderInstances
