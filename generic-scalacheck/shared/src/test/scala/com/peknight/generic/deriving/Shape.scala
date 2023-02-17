package com.peknight.generic.deriving

enum Shape derives CanEqual:
  case Rectangle(width: Int, height: Int)
  case Circle(radius: Int)