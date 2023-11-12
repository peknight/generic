package com.peknight.generic.ciris.instances

import ciris.{ConfigDecoder, ConfigError}
import com.peknight.error.Error
import com.peknight.generic.mapper.MigrationT

import scala.concurrent.duration.{Duration, FiniteDuration}

trait ConfigDecoderInstances3 extends ConfigDecoderInstances4:
  private[this] def mapEither[A, B, C](decoder: ConfigDecoder[A, B],
                                       migration: MigrationT[[T] =>> Either[Error, T], B, C]): ConfigDecoder[A, C] =
    decoder.mapEither((_, b) => migration.migrate(b).left.map(error => ConfigError(error.message)))

  given stringErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], String, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.identity[String], migration)
  given bigDecimalErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], BigDecimal, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBigDecimalConfigDecoder, migration)
  given bigIntErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], BigInt, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBigIntConfigDecoder, migration)
  given booleanErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Boolean, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringBooleanConfigDecoder, migration)
  given byteErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Byte, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringByteConfigDecoder, migration)
  given charErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Char, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringCharConfigDecoder, migration)
  given doubleErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Double, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringDoubleConfigDecoder, migration)
  given durationErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Duration, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringDurationConfigDecoder, migration)
  given finiteDurationErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], FiniteDuration, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringFiniteDurationConfigDecoder, migration)
  given floatErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Float, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringFloatConfigDecoder, migration)
  given intErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Int, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringIntConfigDecoder, migration)
  given longErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Long, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringLongConfigDecoder, migration)
  given shortErrorDecoder[A](using migration: MigrationT[[T] =>> Either[Error, T], Short, A])
  : ConfigDecoder[String, A] =
    mapEither(ConfigDecoder.stringShortConfigDecoder, migration)
end ConfigDecoderInstances3
