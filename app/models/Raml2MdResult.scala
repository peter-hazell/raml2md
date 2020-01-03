package models

sealed trait Raml2MdResult

case class Raml2MdSuccess(markdown: String) extends Raml2MdResult

case class Raml2MdFailure(message: String) extends Raml2MdResult
