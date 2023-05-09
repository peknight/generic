package com.peknight.generic.http4s.instances

import com.peknight.generic.mapper.MigrationT
import org.http4s.{ParseFailure, QueryParamDecoder, Uri}

import java.time.{Period, ZoneId}

trait QueryParamDecoderInstances2 extends QueryParamDecoderInstances3:
  given unitEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Unit, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.unitQueryParamDecoder.emap[A](migration.migrate)
  given booleanEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Boolean, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.booleanQueryParamDecoder.emap[A](migration.migrate)
  given doubleEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Double, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.doubleQueryParamDecoder.emap[A](migration.migrate)
  given floatEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Float, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.floatQueryParamDecoder.emap[A](migration.migrate)
  given shortEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Short, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.shortQueryParamDecoder.emap[A](migration.migrate)
  given intEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Int, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.intQueryParamDecoder.emap[A](migration.migrate)
  given longEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Long, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.longQueryParamDecoder.emap[A](migration.migrate)
  given charEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Char, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.charQueryParamDecoder.emap[A](migration.migrate)
  given stringEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], String, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.stringQueryParamDecoder.emap[A](migration.migrate)
  given uriEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Uri, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.uriQueryParamDecoder.emap[A](migration.migrate)
  given zoneIdEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], ZoneId, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.zoneId.emap[A](migration.migrate)
  given periodEmapDecoder[A](using migration: MigrationT[[T] =>> Either[ParseFailure, T], Period, A])
  : QueryParamDecoder[A] =
    QueryParamDecoder.period.emap[A](migration.migrate)

end QueryParamDecoderInstances2
