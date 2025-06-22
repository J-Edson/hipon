package savings

import java.time.LocalDateTime
import java.time.LocalDate
import java.time.ZoneId

class SavingsInterest {

    Savings savings
    Double interestRate
    InterestFrequency interestFrequency
    LocalDateTime startDate = { -> LocalDateTime.now(ZoneId.of("Asia/Manila")) }()
    LocalDate lastAccrualDate


    static constraints = {
    }

    static mapping = {
        id sqlType:'smallint', generator:'increment'
    }
}
