package savings

import java.time.LocalDateTime
import java.time.ZoneId

class SavingsInterest {

    Savings savings
    Double interestRate
    InterestFrequency interestFrequency
    LocalDateTime startDate = { -> LocalDateTime.now(ZoneId.of("Asia/Manila")) }()
    LocalDateTime lastAccrualDate


    static constraints = {
    }

    static mapping = {
        id sqlType:'smallint', generator:'increment'
    }
}
