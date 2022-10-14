package uk.ac.ed.inf;

/**
 * An exception class that is used when the pizza order is invalid
 */
public class InvalidPizzaCombinationException extends Exception{
    public InvalidPizzaCombinationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
