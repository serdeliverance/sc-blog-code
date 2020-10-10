package services

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BatchService @Inject()()(implicit ec: ExecutionContext) {
  def executeBatchProcessing(): Future[Unit] = ???
}
