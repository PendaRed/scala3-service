package com.jgibbons.svcfirst.clientdto

object RenderAsJson {

  import io.circe.generic.semiauto._
  import io.circe.syntax.EncoderOps
  import io.circe._

  implicit val clRowEnc: Encoder[ClientRowDto] = deriveEncoder

  implicit val clRowDec: Decoder[ClientRowDto] = deriveDecoder

}
