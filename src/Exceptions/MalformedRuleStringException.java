package Exceptions;

public class MalformedRuleStringException extends Exception {

    public MalformedRuleStringException(String s) {
        super(s);
    }
    public MalformedRuleStringException() {
        super();
    }
}
