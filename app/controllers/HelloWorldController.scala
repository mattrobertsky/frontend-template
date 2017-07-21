package controllers

import javax.inject._

import config.FrontendConfig
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}

@Singleton
class HelloWorldController @Inject()(implicit configuration: FrontendConfig, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def helloWorld(): Action[AnyContent] = Action {
    implicit request => Ok(views.html.pages.hello_world())
  }

}
