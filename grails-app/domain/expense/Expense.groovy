package expense

import user.Person
import savings.Savings
import category.Status

class Expense {

    Person client
    String txnName
    Double txnAmt
    Savings creditAcct
    Status status
    ExpenseCategory category
    
    static constraints = {
    }

    static mapping = {
        id sqlType:'smallint', generator:'increment'
    }
}
