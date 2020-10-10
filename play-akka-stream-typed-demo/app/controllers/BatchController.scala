package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{BaseController, ControllerComponents}
import services.BatchService

import scala.concurrent.ExecutionContext

@Singleton
class BatchController @Inject()(val controllerComponents: ControllerComponents, val batchService: BatchService)
                               (implicit ec: ExecutionContext) extends BaseController {

  def batch() = Action {
    batchService.executeBatchProcessing()
    Ok("batch processing") }
}
