package com.peknight.generic.http4s.instances

import com.peknight.generic.mapper.Migration
import org.http4s.{QueryParamDecoder, Uri}

import java.time.{Period, ZoneId}

trait QueryParamDecoderInstances4:
  given unitDecoder[A](using migration: Migration[Unit, A]): QueryParamDecoder[A] =
    QueryParamDecoder.unitQueryParamDecoder.map[A](migration.migrate)
  given booleanDecoder[A](using migration: Migration[Boolean, A]): QueryParamDecoder[A] =
    QueryParamDecoder.booleanQueryParamDecoder.map[A](migration.migrate)
  given doubleDecoder[A](using migration: Migration[Double, A]): QueryParamDecoder[A] =
    QueryParamDecoder.doubleQueryParamDecoder.map[A](migration.migrate)
  given floatDecoder[A](using migration: Migration[Float, A]): QueryParamDecoder[A] =
    QueryParamDecoder.floatQueryParamDecoder.map[A](migration.migrate)
  given shortDecoder[A](using migration: Migration[Short, A]): QueryParamDecoder[A] =
    QueryParamDecoder.shortQueryParamDecoder.map[A](migration.migrate)
  given intDecoder[A](using migration: Migration[Int, A]): QueryParamDecoder[A] =
    QueryParamDecoder.intQueryParamDecoder.map[A](migration.migrate)
  given longDecoder[A](using migration: Migration[Long, A]): QueryParamDecoder[A] =
    QueryParamDecoder.longQueryParamDecoder.map[A](migration.migrate)
  given charDecoder[A](using migration: Migration[Char, A]): QueryParamDecoder[A] =
    QueryParamDecoder.charQueryParamDecoder.map[A](migration.migrate)
  given stringDecoder[A](using migration: Migration[String, A]): QueryParamDecoder[A] =
    QueryParamDecoder.stringQueryParamDecoder.map[A](migration.migrate)
  given uriDecoder[A](using migration: Migration[Uri, A]): QueryParamDecoder[A] =
    QueryParamDecoder.uriQueryParamDecoder.map[A](migration.migrate)
  given zoneIdDecoder[A](using migration: Migration[ZoneId, A]): QueryParamDecoder[A] =
    QueryParamDecoder.zoneId.map[A](migration.migrate)
  given periodDecoder[A](using migration: Migration[Period, A]): QueryParamDecoder[A] =
    QueryParamDecoder.period.map[A](migration.migrate)
end QueryParamDecoderInstances4
