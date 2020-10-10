package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc._

@Singleton
class HealthCheckController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def healthCheck() = Action { implicit request => Ok("running") }
}
