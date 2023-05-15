package com.peknight.generic.ciris.instances

import ciris.{ConfigDecoder, ConfigError}
import com.peknight.generic.mapper.MigrationT

import scala.concurrent.duration.{Duration, FiniteDuration}

trait ConfigDecoderInstances2 extends ConfigDecoderInstances3:
  private[this] def mapEither[A, B, C](decoder: ConfigDecoder[A, B],
                                       migration: MigrationT[[T] =>> Either[ConfigError, T], B, C])
  : ConfigDecoder[A, C] = decoder.mapEither((_, b) => migration.migrate(b))

  given stringEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], String, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.identity[String], migration)
  given bigDecimalEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], BigDecimal, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBigDecimalConfigDecoder, migration)
  given bigIntEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], BigInt, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBigIntConfigDecoder, migration)
  given booleanEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Boolean, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBooleanConfigDecoder, migration)
  given byteEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Byte, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringByteConfigDecoder, migration)
  given charEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Char, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringCharConfigDecoder, migration)
  given doubleEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Double, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringDoubleConfigDecoder, migration)
  given durationEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Duration, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringDurationConfigDecoder, migration)
  given finiteDurationEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], FiniteDuration, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringFiniteDurationConfigDecoder, migration)
  given floatEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Float, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringFloatConfigDecoder, migration)
  given intEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Int, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringIntConfigDecoder, migration)
  given longEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Long, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringLongConfigDecoder, migration)
  given shortEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], Short, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringShortConfigDecoder, migration)
end ConfigDecoderInstances2
