package uk.ac.ed.inf;

import java.util.Arrays;

/**
 * Contains the main method which runs the App for the drone delivery service.
 * Create files containing information on the drone flightpath and the delivery outcomes of a certain day
 * */
public class App
{
    /**
     * The main method for running the entire program for the drone delivery service. Uses the other classes to calculate
     * the flight path and delivery outcomes of that day and stores the information in json and GeoJson files.
     *
     * @param args two String arguments used for getting information from the rest server, and also for
     * making the file names to be that stores the drone flight details and delivery outcomes.
     */
    public static void main( String[] args )
    {
        // get the user arguments, date and the url of the rest server
        String date = "2023-02-04";
        String baseURL = "https://ilp-rest.azurewebsites.net/";
        // set the base address and date for easy access from other classes
        BaseAddressAndDate.setValues(baseURL, date);
        // make an object of class OrderDetails that contains the details of orders of that day
        OrderDetails[] orders = OrderDetails.getOrderDetails(date, baseURL);
        // create the not yet finished delivery details from validating the current orders
        Deliveries[] inCompleteDeliveries = Deliveries.getDeliveries(orders);
        // create the flightPath object based on the incomplete delivery details and their order details
        CalculateFlightPath flightPath = new CalculateFlightPath(inCompleteDeliveries, orders);
        // write the GeoJson file of flightpath using an object of that class
        GeoJSONWriter writer = new GeoJSONWriter();
        writer.writePathToGeoJSON(flightPath.flightPaths);
        // create Json Files of the flight path and the delivery outcomes using a method from CreateJsonFiles class
        CreateJsonFiles.createJson(flightPath.completeDeliveries, flightPath.flightPaths, date);

    }
}