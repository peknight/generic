package com.peknight.generic.deriving.labels

trait FlattenLabelsInstances2:
  given emptyFlattenLabels[A]: FlattenLabels[A] with
    def labels: List[String] = List.empty[String]
  end emptyFlattenLabels
end FlattenLabelsInstances2
