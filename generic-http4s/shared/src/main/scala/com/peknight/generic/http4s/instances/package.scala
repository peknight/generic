package com.peknight.generic.http4s

package object instances:
  object all extends QueryParamEncoderInstances with QueryParamDecoderInstances
  object queryParamEncoder extends QueryParamEncoderInstances
  object queryParamDecoder extends QueryParamDecoderInstances
end instances
