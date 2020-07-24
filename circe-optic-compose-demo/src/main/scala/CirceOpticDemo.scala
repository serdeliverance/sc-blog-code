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
        "person":
        {
          "firstname": "john",
          "lastname": "doe",
          "age": 21,
          "occupation":
           {
              "title": "employee",
              "profession": "software developmer",
              "seniority": 3
           }
        }
      }
    """

  val json = parse(jsonStr)

  def firstname(value: String): Json => Json = root.person.firstname.string.set(value)

  val result: String = json.map(firstname("sergio").andThen(_.toString()))
    .fold(_ => "invalid json", r => r)

  println(result)
}
