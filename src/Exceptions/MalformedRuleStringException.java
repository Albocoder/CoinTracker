package Exceptions;

public class MalformedRuleStringException extends RuntimeException {

    public MalformedRuleStringException(String s) {
        super(s);
    }
    public MalformedRuleStringException() {
        super();
    }
}
