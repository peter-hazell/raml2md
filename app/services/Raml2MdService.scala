package services

import amf.client.model.domain.WebApi
import com.overzealous.remark.{Options, Remark}
import connectors.GitHubConnector
import javax.inject.Inject
import models.{Raml2MdFailure, Raml2MdResult, Raml2MdSuccess}
import webapi.{Raml10, WebApiBaseUnit, WebApiDocument}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class Raml2MdService @Inject()(gitHubConnector: GitHubConnector)(implicit ec: ExecutionContext) {

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

  def mdToHtml(raml2MdResult: Raml2MdResult): Future[Option[String]] =
    raml2MdResult match {
      case Raml2MdSuccess(s) => gitHubConnector.markdownToHtml(s)
      case Raml2MdFailure(f) => Future.successful(None)
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
