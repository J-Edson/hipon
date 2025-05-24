package log

import java.time.LocalDateTime
import java.time.ZoneId

import expense.Expense
import savings.Savings
import user.Person
import category.Status

class Record {

    Person client
    Savings credit
    Savings debit
    String description
    Expense expense
    RecordType recordType
    Status status
    Double txnAmt
    LocalDateTime logDate = { -> LocalDateTime.now(ZoneId.of("Asia/Manila")) }()

    static constraints = {
        credit nullable: true
        debit nullable: true
        expense nullable: true
    }
}
