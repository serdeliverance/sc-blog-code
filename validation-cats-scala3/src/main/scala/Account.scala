import java.time.OffsetDateTime
import cats.data.Validated
import AccountValidator.ValidationResult

case class Account(
    name: String,
    description: Option[String],
    userId: Long,
    initialAmount: BigDecimal,
    createdAt: OffsetDateTime,
    id: Option[Long] = None
)

object Account:

  extension (account: Account)
    def toValidated: ValidationResult[Account] =
      AccountValidator.validate(account)
