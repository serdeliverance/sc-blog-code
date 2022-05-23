package io.github.sdev.example

sealed trait AccountValidation:
  def errorMessage: String

case object NameIsEmpty extends AccountValidation:
  override def errorMessage = "name must not be empty"

case object UserIsInvalid extends AccountValidation:
  override def errorMessage = "userId must be positive"

case object InitialAmountNotPositive extends AccountValidation:
  override def errorMessage = "initial amount must be positive"

case object CreationDateInvalid extends AccountValidation:
  override def errorMessage = "creation date cannot be greater than current time"
