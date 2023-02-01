package com.peknight.generic.constraint

/**
 * Type inequalities
 */
private[generic] trait =:!=[A, B] extends Serializable

private[generic] object =:!= {

  given [A, B]: =:!=[A, B] = new=:!=[A, B] {}
  given neqAmbiguous1[A]: =:!=[A, A] = unexpected
  given neqAmbiguous2[A]: =:!=[A, A] = unexpected
}
