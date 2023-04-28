package com.peknight.generic.deriving.labels

trait FlattenLabelsInstances extends FlattenLabelsInstances2:
  given optionFlattenLabels[A] (using flattenLabels: FlattenLabels[A]): FlattenLabels[Option[A]] with
    def labels: List[String] = flattenLabels.labels
  end optionFlattenLabels
end FlattenLabelsInstances
object FlattenLabelsInstances extends FlattenLabelsInstances
