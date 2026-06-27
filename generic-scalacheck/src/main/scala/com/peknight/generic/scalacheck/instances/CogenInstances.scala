package com.peknight.generic.scalacheck.instances

import com.peknight.generic.Generic
import org.scalacheck.Cogen
import org.scalacheck.rng.Seed

trait CogenInstances:
  given [A](using instances: => Generic.Instances[Cogen, A]): Cogen[A] = instances.derive(
    inst ?=> Cogen { (seed: Seed, a: A) => inst.foldLeft[Seed](a)(seed)(
      [T] => (seed: Seed, cogen: Cogen[T], t: T) => cogen.perturb(seed, t)
    )},
    inst ?=> Cogen { (seed: Seed, a: A) => inst.instance(a).perturb(seed, a) }
  )
end CogenInstances

object CogenInstances extends CogenInstances
