package models.forms
import play.api.data.Form
import play.api.data.Forms._

object RamlForm {

  val ramlForm = Form(single("ramlContents" -> text))

}
