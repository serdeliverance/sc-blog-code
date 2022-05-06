package io.github.sdev.example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import java.time.OffsetDateTime
import cats.data.Validated.Valid
import cats.data.NonEmptyList
import cats.data.Validated.Invalid

class AccountValidatorSpec extends AnyFlatSpec with Matchers {

  private val account = Account("n26-23", 34, 23, OffsetDateTime.now)

  it should "return valid account" in {
    val result = account.toValidated
    result mustBe Valid(account)
  }

  it should "return invalid userId" in {
    val invalidAccount = account.copy(userId = -5) // invalid userId
    val result         = invalidAccount.toValidated
    result match
      case Invalid(nel) => nel.toList mustBe List(UserIsInvalid)
      case _            => fail("userId validation error expected")
  }

  it should "return invalid accumulated list results" in {
    val invalidAccount =
      account.copy(userId = -5, createdAt = OffsetDateTime.now.plusYears(2)) // invalid userId and createdAt
    val result = invalidAccount.toValidated
    result match
      case Invalid(nel) => nel.toList mustBe List(UserIsInvalid, CreationDateInvalid)
      case _            => fail("multiple account validation error expected")
  }
}
