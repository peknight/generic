package com.peknight.generic.deriving

enum JsonValue derives CanEqual:
  case JsonObject(fields: List[(String, JsonValue)])
  case JsonArray(items: List[JsonValue])
  case JsonString(value: String)
  case JsonNumber(value: Double)
  case JsonBoolean(value: Boolean)
  case JsonNull
