package connectors

import javax.inject.Inject
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class GitHubConnector @Inject()(ws: WSClient)(implicit ec: ExecutionContext) {

  def markdownToHtml(markdownContents: String): Future[Option[String]] =
    ws.url("https://api.github.com/markdown/raw")
      .addHttpHeaders("Content-Type" -> "text/plain")
      .post(markdownContents)
      .map(response =>
        response.status match {
          case 200 => Some(response.body)
          case _   => None
      })

}
