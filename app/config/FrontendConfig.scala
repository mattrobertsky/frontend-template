package config

import javax.inject.Inject

import play.api.Configuration

trait AppConfig {
  val analyticsToken: String
  val analyticsHost: String
  val reportAProblemPartialUrl: String
  val reportAProblemNonJSUrl: String
}

class FrontendConfig @Inject()(config: Configuration) extends AppConfig {
  private def loadConfString(key: String) = config.getString(key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  private val contactHost = loadConfString(s"contact-frontend.host")
  private val contactFormServiceIdentifier = loadConfString("appName")

  lazy val analyticsToken: String = loadConfString("google-analytics.token")
  lazy val analyticsHost: String = loadConfString("google-analytics.host")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
}
