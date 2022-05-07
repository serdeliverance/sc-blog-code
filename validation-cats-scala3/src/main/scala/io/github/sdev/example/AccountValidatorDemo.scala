package io.github.sdev.example

import java.time.OffsetDateTime
import AccountValidator.validate

object AccountValidatorDemo extends App:

  val account = Account("n26-acc", -10, -500, OffsetDateTime.now.plusDays(2))

  val result = validate(account)

  println(result)
