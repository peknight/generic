package com.peknight.generic.http4s.instances

import com.peknight.generic.mapper.Migration
import org.http4s.{QueryParamEncoder, Uri}

import java.time.{Period, ZoneId}

trait QueryParamEncoderInstances:
  given booleanEncoder[A](using migration: Migration[A, Boolean]): QueryParamEncoder[A] =
    QueryParamEncoder.booleanQueryParamEncoder.contramap[A](migration.migrate)
  given doubleEncoder[A](using migration: Migration[A, Double]): QueryParamEncoder[A] =
    QueryParamEncoder.doubleQueryParamEncoder.contramap[A](migration.migrate)
  given floatEncoder[A](using migration: Migration[A, Float]): QueryParamEncoder[A] =
    QueryParamEncoder.floatQueryParamEncoder.contramap[A](migration.migrate)
  given shortEncoder[A](using migration: Migration[A, Short]): QueryParamEncoder[A] =
    QueryParamEncoder.shortQueryParamEncoder.contramap[A](migration.migrate)
  given intEncoder[A](using migration: Migration[A, Int]): QueryParamEncoder[A] =
    QueryParamEncoder.intQueryParamEncoder.contramap[A](migration.migrate)
  given longEncoder[A](using migration: Migration[A, Long]): QueryParamEncoder[A] =
    QueryParamEncoder.longQueryParamEncoder.contramap[A](migration.migrate)
  given stringEncoder[A](using migration: Migration[A, String]): QueryParamEncoder[A] =
    QueryParamEncoder.stringQueryParamEncoder.contramap[A](migration.migrate)
  given uriEncoder[A](using migration: Migration[A, Uri]): QueryParamEncoder[A] =
    QueryParamEncoder.uriQueryParamEncoder.contramap[A](migration.migrate)
  given zoneIdEncoder[A](using migration: Migration[A, ZoneId]): QueryParamEncoder[A] =
    QueryParamEncoder.zoneId.contramap[A](migration.migrate)
  given periodEncoder[A](using migration: Migration[A, Period]): QueryParamEncoder[A] =
    QueryParamEncoder.period.contramap[A](migration.migrate)
end QueryParamEncoderInstances
object QueryParamEncoderInstances extends QueryParamEncoderInstances
