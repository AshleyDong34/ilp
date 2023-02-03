package uk.ac.ed.inf;
import com. fasterxml . jackson . databind . ObjectMapper ;
import java . io . IOException ;
import java . net .URL;

/**
 * This class stores the deserialized member variables from the json files stored on the rest server.
 * This is based on the user date and base address url.
 */
public class OrderDetails {

    public String orderNo;
    public String orderDate;
    public String customer;
    public String creditCardNumber;
    public String creditCardExpiry;
    public String cvv;
    public int priceTotalInPence;
    public String[] orderItems;

    private static OrderDetails[] allOrders;

    /**
     * This method deserializes the json file taken from the rest server based on the
     * user input and base url. The result is an array of this class
     * @param date user input date as String
     * @param baseUrl user input Url as String
     * @return and array of this class
     */
    public static OrderDetails[] getOrderDetails(String date, String baseUrl) {

        try {
            baseUrl += "orders/" + date;

            //creates a pointer to the resource on the internet
            URL url = new URL(baseUrl);
            //
            allOrders = new ObjectMapper().readValue(url, OrderDetails[].class);
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return allOrders;
    }

}
