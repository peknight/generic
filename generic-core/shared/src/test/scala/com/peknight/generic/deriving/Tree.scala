package com.peknight.generic.deriving

enum Tree[T] derives CsvEncoder:
  case Branch(left: Tree[T], right: Tree[T])
  case Leaf(elem: T)