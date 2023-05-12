package com.peknight.generic.http4s.instances

import com.peknight.error.std.Error
import com.peknight.generic.mapper.MigrationT
import org.http4s.{ParseFailure, QueryParamDecoder, Uri}

import java.time.{Period, ZoneId}

trait QueryParamDecoderInstances3 extends QueryParamDecoderInstances4:
  private[this] def migrate[A, B](using migration: MigrationT[[T] =>> Either[Error, T], A, B])
  : A => Either[ParseFailure, B] =
    a => migration.migrate(a).left.map(error => ParseFailure(error.message, error.label))

  given unitErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Unit, A]): QueryParamDecoder[A] =
    QueryParamDecoder.unitQueryParamDecoder.emap[A](migrate)
  given booleanErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Boolean, A]): QueryParamDecoder[A] =
    QueryParamDecoder.booleanQueryParamDecoder.emap[A](migrate)
  given doubleErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Double, A]): QueryParamDecoder[A] =
    QueryParamDecoder.doubleQueryParamDecoder.emap[A](migrate)
  given floatErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Float, A]): QueryParamDecoder[A] =
    QueryParamDecoder.floatQueryParamDecoder.emap[A](migrate)
  given shortErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Short, A]): QueryParamDecoder[A] =
    QueryParamDecoder.shortQueryParamDecoder.emap[A](migrate)
  given intErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Int, A]): QueryParamDecoder[A] =
    QueryParamDecoder.intQueryParamDecoder.emap[A](migrate)
  given longErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Long, A]): QueryParamDecoder[A] =
    QueryParamDecoder.longQueryParamDecoder.emap[A](migrate)
  given charErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Char, A]): QueryParamDecoder[A] =
    QueryParamDecoder.charQueryParamDecoder.emap[A](migrate)
  given stringErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], String, A]): QueryParamDecoder[A] =
    QueryParamDecoder.stringQueryParamDecoder.emap[A](migrate)
  given uriErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Uri, A]): QueryParamDecoder[A] =
    QueryParamDecoder.uriQueryParamDecoder.emap[A](migrate)
  given zoneIdErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], ZoneId, A]): QueryParamDecoder[A] =
    QueryParamDecoder.zoneId.emap[A](migrate)
  given periodErrorDecoder[A](using MigrationT[[T] =>> Either[Error, T], Period, A]): QueryParamDecoder[A] =
    QueryParamDecoder.period.emap[A](migrate)

end QueryParamDecoderInstances3
