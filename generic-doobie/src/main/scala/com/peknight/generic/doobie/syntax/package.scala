package com.peknight.generic.doobie

package object syntax:
  object all extends GetSyntax with PutSyntax with MetaSyntax with ReadSyntax with WriteSyntax
  object get extends GetSyntax
  object put extends PutSyntax
  object meta extends MetaSyntax
  object read extends ReadSyntax
  object write extends WriteSyntax
end syntax
