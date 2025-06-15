package savings

import user.Person
import category.Status

class Savings {

    Person client
    String acctName
    String acctNo
    Double balance
    Date expiryDate
    Status status
    
    static constraints = {
        acctNo nullable: true
        expiryDate nullable: true
    }

    static mapping = {
        id sqlType:'smallint', generator:'increment'
    }
}
