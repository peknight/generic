package com.peknight.generic.deriving.labels

import com.peknight.generic.deriving.Generic
import com.peknight.generic.tuple.ops.TupleOps

trait FlattenLabelsInstances2 extends FlattenLabelsInstances3:
  given productFlattenLabels[A] (using generic: Generic.Product.Instances[FlattenLabels, A]): FlattenLabels[A] with
    def labels: List[String] =
      val tuple = generic.labels.zip(generic.instances.map[[_] =>> List[String]] {
        [T] => (t: T) => t.asInstanceOf[FlattenLabels[T]].labels
      })
      TupleOps.foldRight(tuple, List.empty[String]) { [T] => (t: T, acc: List[String]) =>
        val (label, labels) = t.asInstanceOf[(String, List[String])]
        labels match
          case Nil => label :: acc
          case _ => labels ::: acc
      }
  end productFlattenLabels
end FlattenLabelsInstances2
