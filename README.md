# Pek Generic

Scala3泛化编程，由于个人不太喜欢Shapeless3到处都是`Any`的API，
就参考[Shapeless](https://github.com/milessabin/shapeless)自己写了一套简单版的。

## generic-core

#### `com.peknight.generic.deriving`包

参考Shapeless的`ProductInstances`与`CoproductInstances`实现`Generic.Product.Instances`与`Generic.Sum.Instances`。

#### `com.peknight.generic.tuple`包

扩展原生`Tuple`，提供`reverse`、`flatMap`、`traverse`、`sequence`、`foldLeft`、`foldRight`、`mapN`等方法

## generic-migration

参考[The Type Astronaut's Guide to Shapeless Book](https://underscore.io/books/shapeless-guide/)实现的`Migration`类。
使用方式可以参考测试类。

## generic-scalacheck

参考[alexarchambault/scalacheck-shapeless](https://github.com/alexarchambault/scalacheck-shapeless)提供scalacheck的推导实例。
使用方式可以参考测试类。