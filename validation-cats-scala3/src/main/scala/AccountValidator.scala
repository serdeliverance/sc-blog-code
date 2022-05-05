import cats.data.Validated
import cats.data.NonEmptyList
import java.time.OffsetDateTime
object AccountValidator:

  type ValidationResult[T] = Validated[NonEmptyList[AccountValidation], T]

  def validate(account: Account): ValidationResult[Account] = ???
