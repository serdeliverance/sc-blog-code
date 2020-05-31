import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import domain.CinemaTicketMachine
import domain.CinemaTicketMachine.{NOT_AVAILABLE_SEATS, MOVIE_NOT_EXISTS, MovieNotAvailable, MovieOK, MovieRequest}
import domain.MovieTheater.ReservateAll
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class CinemaTicketMachineSpec extends TestKit(ActorSystem("TicketMachineSpec"))
  with ImplicitSender with AnyWordSpecLike with BeforeAndAfterAll{

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A cinema ticket machine" should {

    "respond with movie not available seats message" in {
      val cinemaTicketMachine = system.actorOf(Props[CinemaTicketMachine])
      cinemaTicketMachine ! ReservateAll
      cinemaTicketMachine ! MovieRequest("Iron Man 3")
      expectMsg(MovieNotAvailable(NOT_AVAILABLE_SEATS))
    }

    "respond with movie not exist message" in {
      val cinemaTicketMachine = system.actorOf(Props[CinemaTicketMachine])
      cinemaTicketMachine ! MovieRequest("Terminator")
      expectMsg(MovieNotAvailable(MOVIE_NOT_EXISTS))
    }

    "respond with available seats" in {
      val cinemaTicketMachine = system.actorOf(Props[CinemaTicketMachine])
      cinemaTicketMachine ! MovieRequest("Iron Man 3")
      expectMsgType[MovieOK]
    }
  }
}
