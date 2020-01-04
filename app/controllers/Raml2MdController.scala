package controllers

import javax.inject._
import models.forms.RamlForm._
import play.api.mvc._
import services.Raml2MdService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Raml2MdController @Inject()(cc: ControllerComponents, raml2MdService: Raml2MdService)(
  implicit ec:                        ExecutionContext
) extends AbstractController(cc) {

  def show(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.raml2md(ramlForm, None, None))
  }

  def submit(): Action[AnyContent] = Action.async { implicit request =>
    ramlForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest("Bad request")), { ramlContents =>
        val raml2MdResult = raml2MdService.raml2Md(ramlContents)

        raml2MdService.mdToHtml(raml2MdResult).map(mdHtml => Ok(views.html.raml2md(ramlForm, Some(raml2MdResult), mdHtml)))
      }
    )
  }

}
