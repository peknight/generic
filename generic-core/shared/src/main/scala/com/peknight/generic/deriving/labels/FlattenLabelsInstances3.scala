package com.peknight.generic.deriving.labels

trait FlattenLabelsInstances3:
  given emptyFlattenLabels[A]: FlattenLabels[A] with
    def labels: List[String] = List.empty[String]
  end emptyFlattenLabels
end FlattenLabelsInstances3
