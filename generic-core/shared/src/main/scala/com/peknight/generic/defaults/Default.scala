package com.peknight.generic.defaults

import com.peknight.generic.Mirror
import com.peknight.generic.tuple.Map

import scala.Tuple.Size
import scala.compiletime.constValue
import scala.quoted.{Expr, Quotes, Type, quotes}

/**
 * Original code by Dmytro Mitin, with slight modifications by Simão Martins.
 * See: https://stackoverflow.com/questions/68421043/type-class-derivation-accessing-default-values
 */
trait Default[T] extends Serializable:
  type Repr <: Tuple
  def defaults: Map[Repr, Option]
end Default
object Default:

  type Aux[T, Repr0 <: Tuple] = Default[T] { type Repr = Repr0 }

  transparent inline given mkDefault[T](using mirror: Mirror[T]): Default[T] =
    new Default[T]:
      type Repr = mirror.MirroredElemTypes
      lazy val defaults: Map[Repr, Option] =
        // summon the size of mirror.MirroredElemLabels (not mirror.MirroredElemTypes) because
        // in some rare edge cases, the latter fails (and both always have the same size)
        val size = constValue[Size[mirror.MirroredElemLabels]]
        getDefaults[T](size).asInstanceOf[Map[Repr, Option]]
  end mkDefault

  private[generic] inline def getDefaults[T](inline s: Int): Tuple = ${ getDefaultsImpl[T]('s) }
  private[generic] def getDefaultsImpl[T](s: Expr[Int])(using Quotes, Type[T]): Expr[Tuple] =
    import quotes.reflect.*
    val n = s.asTerm.underlying.asInstanceOf[Literal].constant.value.asInstanceOf[Int]
    val companion = TypeRepr.of[T].typeSymbol.companionModule
    val expressions: List[Expr[Option[Any]]] = List.tabulate(n) { i =>
      val termOpt = companion.declaredMethod(s"$$lessinit$$greater$$default$$${i + 1}").headOption.map { s =>
        val select = Ref(companion).select(s)
        TypeRepr.of[T].typeArgs match
          case Nil => select
          case typeArgs => select.appliedToTypes(typeArgs)
      }
      termOpt match
        case None => Expr(None)
        case Some(et) => '{ Some(${ et.asExpr }) }
    }
    Expr.ofTupleFromSeq(expressions)
  end getDefaultsImpl
end Default
