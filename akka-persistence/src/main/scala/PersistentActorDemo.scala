import PersistentActorDemo.GeekCoinActor.{QuoteUpdated, UpdateQuote, calculateNewQuote}
import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor

object PersistentActorDemo extends App {

  object GeekCoinActor {
    // commands
    case class UpdateQuote(quotePercentage: Double)
    // events
    case class QuoteUpdated(quotePercentage: Double)

    def calculateNewQuote(quote: Double, quotePercentage: Double) = quote + quote * quotePercentage / 100
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
          quote = calculateNewQuote(quote, quotePercentage)
          log.info(f"State updated. New state -> quote: $quote%1.2f")
        }
    }

    override def receiveRecover: Receive = {
      case event @ QuoteUpdated(quotePercentage) =>
        quote = calculateNewQuote(quote, quotePercentage)
        log.info(f"Recovered event: $event. State -> quote: $quote%1.2f")
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
