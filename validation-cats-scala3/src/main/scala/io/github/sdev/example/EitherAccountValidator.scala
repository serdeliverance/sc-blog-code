package io.github.sdev.example

import java.time.OffsetDateTime
object EitherAccountValidator extends App:

  private def validateName(name: String): Either[String, String] =
    Either.cond(name.nonEmpty, name, "name must not be empty")

  private def validateUserId(userId: Long): Either[String, Long] =
    Either.cond(userId > 0, userId, "userId must be positive")

  private def validateInitialAmount(initialAmount: BigDecimal): Either[String, BigDecimal] =
    Either.cond(initialAmount > 0, initialAmount, "initial amount must be positive")

  private def validateCreatedAt(createdAt: OffsetDateTime): Either[String, OffsetDateTime] =
    Either.cond(createdAt.isBefore(OffsetDateTime.now), createdAt, "creation date could not be in the future")

  case class Account(name: String, userId: Long, initialAmount: BigDecimal, createdAt: OffsetDateTime)

  def validate(account: Account): Either[String, Account] =
    for
      name          <- validateName(account.name)
      userId        <- validateUserId(account.userId)
      initialAmount <- validateInitialAmount(account.initialAmount)
      createdAt     <- validateCreatedAt(account.createdAt)
    yield Account(name, userId, initialAmount, createdAt)

  // first example - either short circuiting validations
  val account = Account(
    "pedro",                        // OK
    -20,                            // invalid userId
    -500,                           // negative initial amount
    OffsetDateTime.now.plusYears(2) // creation date is two years in the future!!!
  )

  val result = validate(account)

  println(result)
