package com.peknight.generic.deriving

enum Shape derives CanEqual:
  case Rectangle(width: Double, height: Double)
  case Circle(radius: Double)