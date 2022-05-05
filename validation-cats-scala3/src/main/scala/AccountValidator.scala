import cats.data.Validated
import cats.data.NonEmptyList
import java.time.OffsetDateTime

import cats.data._
import cats.data.Validated._
import cats.implicits._

object AccountValidator:

  type ValidationResult[T] = Validated[NonEmptyChain[AccountValidation], T]

  def validate(account: Account): ValidationResult[Account] = ???

  private def validateName(name: String): ValidationResult[String] =
    if name.nonEmpty then name.validNec else NameIsEmpty.invalidNec

  private def validateUserId(userId: Long): ValidationResult[Long] =
    if userId > 0 then userId.validNec else UserIsInvalid.invalidNec

  private def validateInitialAmount(initialAmount: BigDecimal): ValidationResult[BigDecimal] =
    if initialAmount > 0 then initialAmount.validNec else InitialAmountNotPositive.invalidNec

  private def validateCreatedAt(createdAt: OffsetDateTime): ValidationResult[OffsetDateTime] =
    if createdAt.isBefore(OffsetDateTime.now) then createdAt.validNec else CreationDateInvalid.invalidNec
