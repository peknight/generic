package com.peknight.generic.doobie.instances

import cats.Show
import com.peknight.generic.mapper.MigrationT
import doobie.{Get, Meta}
import org.tpolecat.typename.TypeName

trait GetInstances extends GetInstances2:
  given tegetStringByte[A](using migration: MigrationT[[T] =>> Either[String, T], Byte, A], typeName: TypeName[A])
  : Get[A] =
    Meta[Byte].get.temap(migration.migrate)
  given tegetStringShort[A](using migration: MigrationT[[T] =>> Either[String, T], Short, A], typeName: TypeName[A])
  : Get[A] =
    Meta[Short].get.temap(migration.migrate)
  given tegetStringInt[A](using migration: MigrationT[[T] =>> Either[String, T], Int, A], typeName: TypeName[A])
  : Get[A] =
    Meta[Int].get.temap(migration.migrate)
  given tegetStringLong[A](using migration: MigrationT[[T] =>> Either[String, T], Long, A], typeName: TypeName[A])
  : Get[A] =
    Meta[Long].get.temap(migration.migrate)
  given tegetStringFLoat[A](using migration: MigrationT[[T] =>> Either[String, T], Float, A], typeName: TypeName[A])
  : Get[A] =
    Meta[Float].get.temap(migration.migrate)
  given tegetStringDouble[A](using migration: MigrationT[[T] =>> Either[String, T], Double, A], typeName: TypeName[A])
  : Get[A] =
    Meta[Double].get.temap(migration.migrate)
  given tegetStringBigDecimal[A](using migration: MigrationT[[T] =>> Either[String, T], BigDecimal, A], typeName: TypeName[A])
  : Get[A] =
    Meta[BigDecimal].get.temap(migration.migrate)
  given tegetStringBoolean[A](using migration: MigrationT[[T] =>> Either[String, T], Boolean, A], typeName: TypeName[A])
  : Get[A] =
    Meta[Boolean].get.temap(migration.migrate)
  given tegetStringString[A](using migration: MigrationT[[T] =>> Either[String, T], String, A], typeName: TypeName[A])
  : Get[A] =
    Meta[String].get.temap(migration.migrate)
  given tegetStringByteArray[A](using migration: MigrationT[[T] =>> Either[String, T], Array[Byte], A], typeName: TypeName[A])
  : Get[A] =
    given Show[Array[Byte]] with
      def show(t: Array[Byte]): String = t.mkString(", ")
    end given
    Meta[Array[Byte]].get.temap(migration.migrate)
end GetInstances
object GetInstances extends GetInstances
