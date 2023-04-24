package com.peknight.generic.mapper

object Updater:
  def apply[A, B](f: (A, B) => A): Updater[A, B] = f(_, _)
end Updater
