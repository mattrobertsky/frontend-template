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
  private def loadConfig(key: String) = config.getString(key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  private val contactHost = config.getString(s"contact-frontend.host").getOrElse("")
  private val contactFormServiceIdentifier = loadConfig("appName")

  lazy val analyticsToken = loadConfig("google-analytics.token")
  lazy val analyticsHost = loadConfig("google-analytics.host")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
}
