import CirceOpticDemo.JsonManipulationAPI.{setHotelType, setLocationAddress, setLocationZipCode, setName, setStars}
import io.circe.Json
import io.circe.parser._
import io.circe.optics.JsonPath._

import scala.annotation.tailrec

/*
  A demo for composing changes when parsing json with circe and optics
  using function composition
 */
object CirceOpticDemo extends App {

  val jsonStr =
    """
    {
      "id" : 3305,
      "active" : true,
      "name" : "Voyage Dream",
      "hotel_type" : "premium",
      "stars" : 5,
      "location" : {
        "address" : "500 East A Street South",
        "zipcode" : "69153-3111",
      }
    }
    """

  import JsonManipulationAPI._

  val json = parse(jsonStr)

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

  println(result)

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

    def updateFold(name: String,
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

    def fold(initial: Json)(transformations: List[JsonAction]): Json = {

      @tailrec
      def go(json: Json, tranformations: List[JsonAction]): Json = {
        if (tranformations.isEmpty) json
        else go(tranformations.head(json), tranformations.tail)
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
  }

}
