package domain

import akka.actor.Actor
import domain.MovieTheater._

class MovieTheater extends Actor {

  var seats = List((Seat("A",1), true), (Seat("A",2), true), (Seat("B",1), true), (Seat("B",2), true))

  override def receive: Receive = {
    case GetAvailableSeats =>
      val availableSeats = seats.filter(_._2).map(_._1)
      sender() ! AvialableSeatsResponse(availableSeats)
    case ReservateAll =>
      seats = seats.map(s => (s._1, false))
    case _ => throw new Exception("invalid operation")
  }


}

object MovieTheater {

  case object GetAvailableSeats
  case object ReservateAll

  case class AvialableSeatsResponse(availableSeats: List[Seat])

  case object ReservationAllOK
}
