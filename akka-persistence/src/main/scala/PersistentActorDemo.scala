import PersistentActorDemo.GeekCoinActor.{QuoteUpdated, UpdateQuote}
import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor

object PersistentActorDemo extends App {

  object GeekCoinActor {
    // commands
    case class UpdateQuote(quotePercentage: Double)
    // events
    case class QuoteUpdated(quotePercentage: Double)
  }

  class GeekCoinActor extends PersistentActor with ActorLogging {

    var quote: Double = 100

    override def persistenceId: String = "geekcoin"

    override def receiveCommand: Receive = {
      case UpdateQuote(quotePercentage) =>
        log.info(s"Received UpdateQuote: $quotePercentage")
        // after the event is persisted...
        persist(QuoteUpdated(quotePercentage)) { event =>
          log.info(s"Event persisted: $event")
          // ...we update the state
          quote = quote + quote * quotePercentage
          log.info(s"State updated. New state -> quote: $quote")
        }
    }

    override def receiveRecover: Receive = {
      case event @ QuoteUpdated(quotePercentage) =>
        log.info(s"Recovered event: $event")
        quote = quote + quote * quotePercentage
    }
  }

  // just for testing
  val system = ActorSystem("PersistentActorDemo")
  val geekCoinActor = system.actorOf(Props[GeekCoinActor], "geekCoinActor")

  // sending commands to our actor
  geekCoinActor ! UpdateQuote(50.0)
  geekCoinActor ! UpdateQuote(-15.0)
  geekCoinActor ! UpdateQuote(200.0)
  geekCoinActor ! UpdateQuote(-45.0)
  geekCoinActor ! UpdateQuote(235.0)
}
