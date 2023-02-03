package uk.ac.ed.inf;

/**
 * This is a wrapper class used to return two variables in a function in the CalculateFlightPath class
 */
public class  WrapperClass {
    public Deliveries[] deliveries;
    public OrderDetails[] orderDetails;

    /**
     * This constructor method sets the members of the class instance to the two variables we want
     * to be used when returned from a method.
     * @param deliveries an array of Deliveries
     * @param orderDetails an array of OrderDetails
     */
    public WrapperClass(Deliveries[] deliveries, OrderDetails[] orderDetails ) {
        this.deliveries = deliveries;
        this.orderDetails = orderDetails;
    }
}
