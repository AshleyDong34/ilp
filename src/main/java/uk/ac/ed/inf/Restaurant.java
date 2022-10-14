package uk.ac.ed.inf;

import com. fasterxml . jackson . databind . ObjectMapper ;
import java . io . IOException ;
import java . net .URL;

/**
 * This class stores the name, menu and coordinate details of restaurants, and provides a way
 * to get participating restaurants from the url
 */
public class Restaurant {

    public String name;
    public double longitude;
    public double latitude;
    public Menu[] menu;
    private static Restaurant[] participatingRestaurants;

    /**
     * This method deserializes the URL class arg provided and turns the json string into an array of the Restaurant class,
     * this is now assigned to the static variable participatingRestaurants
     * @param serverBaseAddress is a pointer to a resource to the web
     * @return an array of participating restaurants
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) {
        try {
            // assign the deserialized data to the variable participatingRestaurants
            participatingRestaurants = new ObjectMapper().readValue(serverBaseAddress, Restaurant[].class);
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return participatingRestaurants;
    }

    /**
     * A getter for the menu of this restaurant
     * @return and array of menu objects for this restaurant
     */
    public Menu[] getMenu() {
        return menu;
    }
}
