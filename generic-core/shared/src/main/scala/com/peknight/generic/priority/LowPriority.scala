package com.peknight.generic.priority

case class LowPriority[+A](instance: A) extends AnyVal
