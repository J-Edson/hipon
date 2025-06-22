package savings

class InterestFrequency {

    Integer code
    String name

    static constraints = {
    }

    static mapping = {
        id sqlType:'smallint', generator:'increment'
    }
}
