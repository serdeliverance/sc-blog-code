import java.time.OffsetDateTime

case class AccountDTO(name: String, userId: Long, initialAmount: BigDecimal, createdAt: OffsetDateTime)
