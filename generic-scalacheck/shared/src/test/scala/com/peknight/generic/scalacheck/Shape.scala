package com.peknight.generic.scalacheck

enum Shape derives CanEqual:
  case Rectangle(width: Double, height: Double)
  case Circle(radius: Double)