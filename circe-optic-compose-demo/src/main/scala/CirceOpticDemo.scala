import io.circe.Json
import io.circe.parser._

import io.circe.optics.JsonPath._

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

  val result: String = json.map(enabled(true).andThen(_.toString()))
    .fold(_ => "invalid json", r => r)

  // way 1: chaining operations

  val result1: String = json.map(
    name("The Lost Wonder")
      .andThen(hotelType("Premium"))
      .andThen(stars(5))
      .andThen(locationAddress("Fake Street 123"))
      .andThen(locationZipCode("1212-003"))
      .andThen(_.toString())
  ).fold(_ => "invalid json", r => r)

  println(result)

  object JsonManipulationAPI {

    // some transformations
    def name(name: String): Json => Json = root.name.string.set(name)

    def hotelType(hotelType: String): Json => Json = root.hotel_type.string.set(hotelType)

    def stars(stars: Int): Json => Json = root.stars.int.set(stars)

    def enabled(active: Boolean): Json => Json = root.active.boolean.set(active)

    def locationAddress(address: String): Json => Json = root.location.address.string.set(address)

    def locationZipCode(zipCode: String): Json => Json = root.location.zipcode.string.set(zipCode)
  }

}
