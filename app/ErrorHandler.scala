import javax.inject.{Inject, Singleton}

import config.FrontendConfig
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.http.Status._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Results._
import play.api.mvc.{Request, RequestHeader, Result}
import uk.gov.hmrc.play.http.{HttpException, Upstream4xxResponse, Upstream5xxResponse}

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject ()(implicit configuration: FrontendConfig, val messagesApi: MessagesApi) extends HttpErrorHandler with I18nSupport {

  case class ErrorResponse(statusCode: Int, message: String, xStatusCode: Option[String] = None, requested: Option[String] = None)

  implicit val erFormats: OFormat[ErrorResponse] = Json.format[ErrorResponse]

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {

    Future.successful(
      Status(statusCode) (views.html.error_template("foo", "bar", "foobar")) // todo tidy up the foo bar
    )
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    val errorResponse = exception match {
      case e: HttpException => ErrorResponse(e.responseCode, e.getMessage)
      case e: Upstream4xxResponse => ErrorResponse(e.reportAs, e.getMessage)
      case e: Upstream5xxResponse => ErrorResponse(e.reportAs, e.getMessage)
      case e: Throwable => ErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage)
    }
    Future.successful(new Status(errorResponse.statusCode)(Json.toJson(errorResponse)))
  }
}

