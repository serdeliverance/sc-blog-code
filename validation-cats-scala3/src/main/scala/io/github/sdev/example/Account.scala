package io.github.sdev.example

import java.time.OffsetDateTime
import cats.data.Validated
import AccountValidator.ValidationResult

case class Account(
    name: String,
    userId: Long,
    initialAmount: BigDecimal,
    createdAt: OffsetDateTime
)

object Account:

  extension (account: Account)
    def toValidated: ValidationResult[Account] =
      AccountValidator.validate(account)
