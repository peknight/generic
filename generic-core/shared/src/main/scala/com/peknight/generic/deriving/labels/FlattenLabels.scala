package com.peknight.generic.deriving.labels

trait FlattenLabels[A]:
  def labels: List[String]
end FlattenLabels

object FlattenLabels extends FlattenLabelsInstances
