package com.peknight.generic

import com.peknight.generic.tuple.{LiftP, summonAsTuple, summonValuesAsTuple}

sealed trait Generic[F[_], A]:
  type Labels <: Tuple
  type Repr <: Tuple
  type MirrorType <: Mirror.Labelled[A, Labels, Repr]

  def mirror: MirrorType
  def instances: LiftP[F, Repr]
  inline def labels: Labels = summonValuesAsTuple[Labels]

  inline def derive(f: => Generic.Product[F, A] ?=> F[A], g: => Generic.Sum[F, A] ?=> F[A]): F[A] =
    inline this match
      case product: Generic.Product[F, A] => f(using product)
      case sum: Generic.Sum[F, A] => g(using sum)
  end derive
end Generic

object Generic:

  sealed abstract class Aux[F[_], A, MirrorType0 <: Mirror.Labelled[A, Labels0, Repr0], Labels0 <: Tuple, Repr0 <: Tuple](
    val mirror: MirrorType0, inst: () => LiftP[F, Repr0]
  ) extends Generic[F, A]:
    type MirrorType = MirrorType0
    type Labels = Labels0
    type Repr = Repr0
    lazy val instances: LiftP[F, Repr] = inst()
  end Aux

  sealed trait Product[F[_], A] extends Generic[F, A]:
    type MirrorType = Mirror.Product.Labelled[A, Labels, Repr]
  end Product

  object Product:
    final class Aux[F[_], A, Labels0 <: Tuple, Repr0 <: Tuple](
      override val mirror: Mirror.Product.Labelled[A, Labels0, Repr0],
      inst: () => LiftP[F, Repr0]
    ) extends Generic.Aux[F, A, Mirror.Product.Labelled[A, Labels0, Repr0], Labels0, Repr0](mirror, inst)
      with Product[F, A]
  end Product

  sealed trait Sum[F[_], A] extends Generic[F, A]:
    type MirrorType = Mirror.Sum.Labelled[A, Labels, Repr]
    def ordinal(a: A): F[A] = instances.productElement(mirror.ordinal(a)).asInstanceOf[F[A]]
  end Sum

  object Sum:
    final class Aux[F[_], A, Labels0 <: Tuple, Repr0 <: Tuple](
      override val mirror: Mirror.Sum.Labelled[A, Labels0, Repr0],
      inst: () => LiftP[F, Repr0]
    ) extends Generic.Aux[F, A, Mirror.Sum.Labelled[A, Labels0, Repr0], Labels0, Repr0](mirror, inst)
      with Sum[F, A]
  end Sum

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Product.Labelled[A, Labels, Repr]
  ): Generic.Product.Aux[F, A, Labels, Repr] =
    new Generic.Product.Aux[F, A, Labels, Repr](mirror, () => summonAsTuple[LiftP[F, Repr]])

  inline given [F[_], A, Labels <: Tuple, Repr <: Tuple](
    using mirror: Mirror.Sum.Labelled[A, Labels, Repr]
  ): Generic.Sum.Aux[F, A, Labels, Repr] =
    new Generic.Sum.Aux[F, A, Labels, Repr](mirror, () => summonAsTuple[LiftP[F, Repr]])

end Generic
