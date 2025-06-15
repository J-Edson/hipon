package log

class RecordType {

    Integer code
    String name

    static constraints = {
    }

    static mapping = {
        id sqlType:'smallint', generator:'increment'
    }
}
