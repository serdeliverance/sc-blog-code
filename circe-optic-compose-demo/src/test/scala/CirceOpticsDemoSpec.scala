import CirceOpticDemo.JsonManipulationAPI._
import CirceOpticsDemoSpec.{extractLikes, extractName, extractStars, jsonStr}
import io.circe.Json
import io.circe.parser._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CirceOpticsDemoSpec extends AnyWordSpec with Matchers {

  "multiples transformations run concurrently" should {
    "perform in a thread-safe manner" in {

      var modifiedJson1 = Json.Null
      var modifiedJson2 = Json.Null

      val json = parse(jsonStr).getOrElse(Json.Null)

      for (i <- 1 to 1000) {

        val thread1 = new Thread(
          () => modifiedJson1 = updateNameStarsAndLikes(s"The Lost Wonder ${i}", 3, i)(json))
        val thread2 =
          new Thread(
            () => modifiedJson2 = updateNameStarsAndLikes(s"The Lost Wonder ${i + 1000}", 5, i + 1000)(json))

        thread1.start()
        thread2.start()
      }

      Thread.sleep(3000)

      val name1 = extractName(modifiedJson1)
      val stars1 = extractStars(modifiedJson1)
      val likes1 = extractLikes(modifiedJson1)

      val name2 = extractName(modifiedJson2)
      val stars2 = extractStars(modifiedJson2)
      val likes2 = extractLikes(modifiedJson2)

      name1 mustBe "The Lost Wonder 1000"
      stars1 mustBe 3
      likes1 mustBe 1000

      name2 mustBe "The Lost Wonder 2000"
      stars2 mustBe 5
      likes2 mustBe 2000
    }
  }
}

object CirceOpticsDemoSpec {
  val jsonStr =
    """
    {
      "id": 3305,
      "active": true,
      "name": "Voyage Dream",
      "hotel_type": "premium",
      "stars": 5,
      "likes": 0,
      "location": {
        "address": "500 East A Street South",
        "zipcode": "69153-3111"
      }
    }
    """

  def extractName(json: Json) =
    json.hcursor
      .downField("name")
      .as[String]
      .getOrElse("")

  // auxiliar methods for extracting results
  def extractStars(json: Json) =
    json.hcursor
      .downField("stars")
      .as[Int]
      .getOrElse("-1")

  def extractLikes(json: Json) =
    json.hcursor
      .downField("likes")
      .as[Int]
      .getOrElse("-1")
}

