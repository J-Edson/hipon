package expense

class ExpenseCategory {

    Integer code
    String name
    
    static constraints = {
    }

    static mapping = {
        id sqlType:'smallint', generator:'increment'
    }
}
