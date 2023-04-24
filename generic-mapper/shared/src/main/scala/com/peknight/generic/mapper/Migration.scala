package com.peknight.generic.mapper

object Migration:
  def apply[A, B](f: A => B): Migration[A, B] = f(_)
end Migration
