package com.peknight.generic.priority

case class HighPriority[+A](instance: A) extends AnyVal
