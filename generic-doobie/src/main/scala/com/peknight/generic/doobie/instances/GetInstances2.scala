package com.peknight.generic.doobie.instances

import cats.Show
import com.peknight.error.std.Error
import com.peknight.generic.mapper.MigrationT
import doobie.{Get, Meta}
import org.tpolecat.typename.TypeName

trait GetInstances2 extends GetInstances3:
  private[this] def temap[A, B](get: Get[A], migration: MigrationT[[T] =>> Either[Error, T], A, B])
                               (using Show[A], TypeName[A], TypeName[B]): Get[B] =
    get.temap(a => migration.migrate(a).left.map(_.message))
  given tegetErrorByte[A](using migration: MigrationT[[T] =>> Either[Error, T], Byte, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[Byte].get, migration)
  given tegetErrorShort[A](using migration: MigrationT[[T] =>> Either[Error, T], Short, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[Short].get, migration)
  given tegetErrorInt[A](using migration: MigrationT[[T] =>> Either[Error, T], Int, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[Int].get, migration)
  given tegetErrorLong[A](using migration: MigrationT[[T] =>> Either[Error, T], Long, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[Long].get, migration)
  given tegetErrorFLoat[A](using migration: MigrationT[[T] =>> Either[Error, T], Float, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[Float].get, migration)
  given tegetErrorDouble[A](using migration: MigrationT[[T] =>> Either[Error, T], Double, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[Double].get, migration)
  given tegetErrorBigDecimal[A](using migration: MigrationT[[T] =>> Either[Error, T], BigDecimal, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[BigDecimal].get, migration)
  given tegetErrorBoolean[A](using migration: MigrationT[[T] =>> Either[Error, T], Boolean, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[Boolean].get, migration)
  given tegetErrorString[A](using migration: MigrationT[[T] =>> Either[Error, T], String, A], typeName: TypeName[A])
  : Get[A] =
    temap(Meta[String].get, migration)
  given tegetErrorByteArray[A](using migration: MigrationT[[T] =>> Either[Error, T], Array[Byte], A], typeName: TypeName[A])
  : Get[A] =
    given Show[Array[Byte]] with
      def show(t: Array[Byte]): String = t.mkString(", ")
    end given
    temap(Meta[Array[Byte]].get, migration)
end GetInstances2
