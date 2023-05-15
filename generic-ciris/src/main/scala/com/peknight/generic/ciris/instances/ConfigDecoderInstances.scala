package com.peknight.generic.ciris.instances

import ciris.{ConfigDecoder, ConfigError, ConfigKey}
import com.peknight.generic.mapper.MigrationT

import scala.concurrent.duration.{Duration, FiniteDuration}

trait ConfigDecoderInstances extends ConfigDecoderInstances2:
  private[this] def mapEither[A, B, C](decoder: ConfigDecoder[A, B],
                                       migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], B), C])
  : ConfigDecoder[A, C] =
    decoder.mapEither((configKeyOption, b) => migration.migrate((configKeyOption, b)))

  given stringOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], String), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.identity[String], migration)
  given bigDecimalOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], BigDecimal), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBigDecimalConfigDecoder, migration)
  given bigIntOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], BigInt), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBigIntConfigDecoder, migration)
  given booleanOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Boolean), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBooleanConfigDecoder, migration)
  given byteOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Byte), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringByteConfigDecoder, migration)
  given charOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Char), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringCharConfigDecoder, migration)
  given doubleOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Double), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringDoubleConfigDecoder, migration)
  given durationOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Duration), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringDurationConfigDecoder, migration)
  given finiteDurationOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], FiniteDuration), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringFiniteDurationConfigDecoder, migration)
  given floatOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Float), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringFloatConfigDecoder, migration)
  given intOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Int), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringIntConfigDecoder, migration)
  given longOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Long), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringLongConfigDecoder, migration)
  given shortOptionEitherDecoder[A](using migration: MigrationT[[T] =>> Either[ConfigError, T], (Option[ConfigKey], Short), A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringShortConfigDecoder, migration)
end ConfigDecoderInstances
object ConfigDecoderInstances extends ConfigDecoderInstances
