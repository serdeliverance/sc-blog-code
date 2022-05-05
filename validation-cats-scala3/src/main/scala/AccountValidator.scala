import cats.data.Validated
import cats.data.NonEmptyList
import java.time.OffsetDateTime

import cats.data._
import cats.data.Validated._
import cats.implicits._

object AccountValidator:

  type ValidationResult[T] = ValidatedNel[AccountValidation, T]

  def validate(account: Account): ValidationResult[Account] = ???

  private def validateName(name: String): ValidationResult[String] =
    if name.nonEmpty then name.validNel else NameIsEmpty.invalidNel

  private def validateUserId(userId: Long): ValidationResult[Long] =
    if userId > 0 then userId.validNel else UserIsInvalid.invalidNel

  private def validateInitialAmount(initialAmount: BigDecimal): ValidationResult[BigDecimal] =
    if initialAmount > 0 then initialAmount.validNel else InitialAmountNotPositive.invalidNel

  private def validateCreatedAt(createdAt: OffsetDateTime): ValidationResult[OffsetDateTime] =
    if createdAt.isBefore(OffsetDateTime.now) then createdAt.validNel else CreationDateInvalid.invalidNel

  def validate(accountDTO: AccountDTO): ValidationResult[Account] =
    (
      validateName(accountDTO.name),
      validateUserId(accountDTO.userId),
      validateInitialAmount(accountDTO.initialAmount),
      validateCreatedAt(accountDTO.createdAt)
    )
      .mapN((name, userId, initialAmount, createdAt) => Account(name, userId, initialAmount, createdAt))
