package com.peknight.generic.http4s.instances

import cats.data.ValidatedNel
import com.peknight.generic.mapper.MigrationT
import org.http4s.{ParseFailure, QueryParamDecoder, Uri}

import java.time.{Period, ZoneId}

trait QueryParamDecoderInstances extends QueryParamDecoderInstances2:
  given unitEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Unit, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.unitQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given booleanEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Boolean, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.booleanQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given doubleEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Double, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.doubleQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given floatEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Float, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.floatQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given shortEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Short, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.shortQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given intEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Int, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.intQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given longEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Long, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.longQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given charEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Char, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.charQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given stringEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], String, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.stringQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given uriEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Uri, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.uriQueryParamDecoder.emapValidatedNel[A](migration.migrate)
  given zoneIdEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], ZoneId, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.zoneId.emapValidatedNel[A](migration.migrate)
  given periodEmapValidatedNelDecoder[A](using migration: MigrationT[[T] =>> ValidatedNel[ParseFailure, T], Period, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.period.emapValidatedNel[A](migration.migrate)

end QueryParamDecoderInstances
object QueryParamDecoderInstances extends QueryParamDecoderInstances
