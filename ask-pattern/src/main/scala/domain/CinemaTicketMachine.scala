package domain

import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.util.Timeout
import domain.CinemaTicketMachine._
import domain.MovieTheater._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.util.Success

class CinemaTicketMachine extends Actor {

  implicit val timeout: Timeout = Timeout(2 seconds)
  implicit val executionContext: ExecutionContext = context.dispatcher

  val movieCatalog = initializeMovies()

  val movieTheather = context.actorOf(Props[MovieTheater], MOVIE_THEATER_ACTOR_NAME)

  override def receive: Receive = {
    case MovieRequest(movie) if movieCatalog.contains(movie) =>
      val replyTo = sender()
      val future = movieTheather.ask(GetAvailableSeats)
      future.onComplete {
        case Success(response) => response match {
          case AvialableSeatsResponse(seats) =>
            if (seats.isEmpty) replyTo ! MovieNotAvailable(NOT_AVAILABLE_SEATS)
            else replyTo ! MovieOK(seats)
        }
      }

    case ReservateAll =>
      movieTheather ! ReservateAll
    case _ => sender() ! MovieNotAvailable(MOVIE_NOT_EXISTS)
  }
}

object CinemaTicketMachine {

  case class MovieRequest(movie: String)

  case class MovieOK(availableSteats: List[Seat])
  case class MovieNotAvailable(reason: String)

  val MOVIE_NOT_EXISTS = "movie not exists"
  val NOT_AVAILABLE_SEATS = "the requested movie has not available seats"

  def initializeMovies() = List("Gravity", "Iron Man 3", "Inside Llewyn Davis", "The Hobbit: The Desolation of Smaug", "The World's End", "World War Z")
}
