package com.peknight.generic.ciris.instances

import ciris.ConfigDecoder
import com.peknight.generic.mapper.Migration

import scala.concurrent.duration.{Duration, FiniteDuration}

trait ConfigDecoderInstances4:
  given stringDecoder[A](using migration: Migration[String, A]): ConfigDecoder[String, A] =
    ConfigDecoder.identity[String].map(migration.migrate)
  given bigDecimalDecoder[A](using migration: Migration[BigDecimal, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringBigDecimalConfigDecoder.map(migration.migrate)
  given bigIntDecoder[A](using migration: Migration[BigInt, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringBigIntConfigDecoder.map(migration.migrate)
  given booleanDecoder[A](using migration: Migration[Boolean, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringBooleanConfigDecoder.map(migration.migrate)
  given byteDecoder[A](using migration: Migration[Byte, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringByteConfigDecoder.map(migration.migrate)
  given charDecoder[A](using migration: Migration[Char, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringCharConfigDecoder.map(migration.migrate)
  given doubleDecoder[A](using migration: Migration[Double, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringDoubleConfigDecoder.map(migration.migrate)
  given durationDecoder[A](using migration: Migration[Duration, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringDurationConfigDecoder.map(migration.migrate)
  given finiteDurationDecoder[A](using migration: Migration[FiniteDuration, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringFiniteDurationConfigDecoder.map(migration.migrate)
  given floatDecoder[A](using migration: Migration[Float, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringFloatConfigDecoder.map(migration.migrate)
  given intDecoder[A](using migration: Migration[Int, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringIntConfigDecoder.map(migration.migrate)
  given longDecoder[A](using migration: Migration[Long, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringLongConfigDecoder.map(migration.migrate)
  given shortDecoder[A](using migration: Migration[Short, A]): ConfigDecoder[String, A] =
    ConfigDecoder.stringShortConfigDecoder.map(migration.migrate)
end ConfigDecoderInstances4
