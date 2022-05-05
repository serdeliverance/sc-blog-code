import cats.data.Validated
import cats.data.NonEmptyList
import java.time.OffsetDateTime

import cats.data._
import cats.data.Validated._
import cats.implicits._

object AccountValidator:

  type ValidationResult[T] = Validated[NonEmptyList[AccountValidation], T]

  def validate(account: Account): ValidationResult[Account] = ???

  private def validateName(name: String): Validated[String, String] =
    Either.cond(name.nonEmpty, name, "name must not be empty").toValidated

  private def validateUserId(userId: Long): Validated[String, Long] =
    Either.cond(userId > 0, userId, "userId must be positive").toValidated

  private def validateInitialAmount(initialAmount: BigDecimal): Validated[String, BigDecimal] =
    Either.cond(initialAmount > 0, initialAmount, "initial amount must be positive").toValidated

  private def validateCreatedAt(createdAt: OffsetDateTime): Validated[String, OffsetDateTime] =
    Either
      .cond(createdAt.isBefore(OffsetDateTime.now), createdAt, "creation date could not be in the future")
      .toValidated
