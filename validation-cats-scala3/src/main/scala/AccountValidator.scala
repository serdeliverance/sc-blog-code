import cats.data.Validated
import cats.data.NonEmptyList
object AccountValidator:

  type ValidationResult[T] = Validated[NonEmptyList[AccountValidation], T]

  def validate(account: Account): ValidationResult[Account] = ???
