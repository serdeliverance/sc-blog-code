import io.circe.Json
import io.circe.optics.JsonPath._
import io.circe.parser._

import scala.annotation.tailrec

/*
  A demo for composing changes when parsing json with circe and optics
  using function composition
 */
object CirceOpticDemo extends App {

  val jsonStr =
    """
    {
      "id": 3305,
      "active": true,
      "name": "Voyage Dream",
      "hotel_type": "premium",
      "stars": 5,
      "likes": 1000,
      "location": {
        "address": "500 East A Street South",
        "zipcode": "69153-3111"
      }
    }
    """

  val json = parse(jsonStr)


  // approach 0: using cursors
  val doc = json.getOrElse(Json.Null)

  val cursor = doc.hcursor

  // modifying just one field using cursors

//  val result = cursor
//    .downField("name").withFocus(_.mapString(_ => "The Lost Wonder")).top
//    .fold("parser error") (_.toString())

  // multiple field modifications using cursors

  val result0_0 = cursor
    .downField("name").withFocus(_.mapString(_ => "The Lost Wonder")).top
    .flatMap(json => json.hcursor.downField("hotel_type").withFocus(_.mapString(_ => "premium")).top)
    .flatMap(json => json.hcursor.downField("stars").withFocus(_ => Json.fromInt(5)).top)
    .fold("parser error")(_.toString())

  // approach 0.1: defining transformations with cursors

  def updateName(name: String): Json => Option[Json] =
    json => json.hcursor.downField("name")
              .withFocus(_.mapString(_ => name)).top
  def updateHotelType(hotelType: String): Json => Option[Json] =
    json => json.hcursor.downField("hotel_type")
              .withFocus(_.mapString(_ => hotelType)).top

  def updateStars(stars: Int): Json => Option[Json] =
    json => json.hcursor.downField("stars")
      .withFocus(_ => Json.fromInt(stars)).top

  // modifying multiple fields using cursors and for comprehension

  val result0_1 = for {
    step1 <- updateName("The Lost Wonder")(doc)
    step2 <- updateHotelType("premium") (step1)
    jsonResult <- updateStars(5) (step2)
  } yield jsonResult

  println(result0_1.fold("parser error")(_.toString))

  import JsonManipulationAPI._

  val result: String = json.map(isEnabled(true).andThen(_.toString()))
    .fold(_ => "invalid json", r => r)

  // way 1: chaining operations
  val result1: String = json.map(
    setName("The Lost Wonder")
      .andThen(setHotelType("Premium"))
      .andThen(setStars(5))
      .andThen(setLocationAddress("Fake Street 123"))
      .andThen(setLocationZipCode("1212-003"))
      .andThen(_.toString())
  ).fold(_ => "invalid json", r => r)

  println(result1)

  // way 2: using a method
  val result2: String = json.map(update(
    name = "The Lost Wonder",
    hotelType = "Premium",
    stars = 5,
    address = "Fake Street 123",
    zipCode = "1212-003"
  ).andThen(_.toString()))
    .fold(_ => "invalid json", r => r)

  object JsonManipulationAPI {

    type JsonAction = Json => Json

    // transformation pipeline
    def update(name: String,
               hotelType: String,
               stars: Int,
               address: String,
               zipCode: String): JsonAction =
      setName(name)
        .andThen(setHotelType(hotelType))
        .andThen(setStars(stars))
        .andThen(setLocationAddress(address))
        .andThen(setLocationZipCode(zipCode))

    // transformation API using fold
    def updateF(name: String,
                hotelType: String,
                stars: Int,
                address: String,
                zipCode: String): JsonAction =
      fold(_)(List(
        setName(name),
        setHotelType(hotelType),
        setStars(stars),
        setLocationAddress(address),
        setLocationZipCode(zipCode)
      ))

    private def fold(initial: Json)(transformations: List[JsonAction]): Json = {

      @tailrec
      def go(json: Json, transformations: List[JsonAction]): Json = {
        if (transformations.isEmpty) json
        else go(transformations.head(json), transformations.tail)
      }

      go(initial, transformations)
    }

    // transformations
    def setName(name: String): JsonAction = root.name.string.set(name)

    def setHotelType(hotelType: String): JsonAction = root.hotel_type.string.set(hotelType)

    def setStars(stars: Int): JsonAction = root.stars.int.set(stars)

    def isEnabled(active: Boolean): JsonAction = root.active.boolean.set(active)

    def setLocationAddress(address: String): JsonAction = root.location.address.string.set(address)

    def setLocationZipCode(zipCode: String): JsonAction = root.location.zipcode.string.set(zipCode)

    def setLikes(likes: Int): JsonAction = root.likes.int.set(likes)

    def updateNameStarsAndLikes(name: String, stars: Int, likes: Int): JsonAction =
      fold(_)(List(
        setName(name),
        setStars(stars),
        setLikes(likes)
      ))
  }

}
