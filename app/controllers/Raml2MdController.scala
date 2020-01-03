package controllers

import amf.client.model.domain.WebApi
import com.overzealous.remark.{Options, Remark}
import javax.inject._
import models.forms.RamlForm._
import models.{Raml2MdFailure, Raml2MdResult, Raml2MdSuccess}
import play.api.mvc._
import webapi.{Raml10, WebApiBaseUnit, WebApiDocument}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

@Singleton
class Raml2MdController @Inject()(cc: ControllerComponents)(
  implicit ec:                        ExecutionContext
) extends AbstractController(cc) {

  def show(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.raml2md(ramlForm, None))
  }

  def submit(): Action[AnyContent] = Action { implicit request =>
    ramlForm.bindFromRequest.fold(formWithErrors => BadRequest("Bad request"), { ramlContents =>
      Ok(views.html.raml2md(ramlForm, Some(raml2Md(ramlContents))))
    })
  }

  def raml2Md(ramlContents: String): Raml2MdResult =
    Try {
      val apiBaseUnit: WebApiBaseUnit = Raml10.parse(ramlContents).get
      val apiDocument: WebApiDocument =
        apiBaseUnit.asInstanceOf[WebApiDocument]
      val api: WebApi = apiDocument.encodes.asInstanceOf[WebApi]

      api2Md(api)
    } match {
      case Success(e) => Raml2MdSuccess(e)
      case Failure(e) => Raml2MdFailure(e.getMessage)
    }

  private def api2Md(api: WebApi): String = {
    val remark = new Remark(Options.pegdownAllExtensions())

    val mdContents = new StringBuilder()

    val apiContents: StringBuilder =
      mdContents.append(
        remark.convertFragment(views.html.api.render(api).toString())
      )

    api.endPoints.forEach(
      endpoint =>
        endpoint.operations.forEach(operation => {
          val operationContents = remark.convertFragment(
            views.html.endpoint_operation
              .render(endpoint, operation)
              .toString()
          )

          mdContents.append(s"\n\n$operationContents")
        })
    )

    mdContents.mkString
  }

}
