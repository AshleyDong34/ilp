package uk.ac.ed.inf;

import com. fasterxml . jackson . databind . ObjectMapper ;
import java . io . IOException ;
import java . net .URL;

public class Restaurant {

    public String name;
    public double longitude;
    public double latitude;
    Menu[] menu;
    private static Restaurant[] participatingRestaurants;

    /**
     * This method deserializes the URL class arg provided and turns the json string into an array of the Restaurant class,
     * this is now assigned to the static variable participatingRestaurants
     * @param serverBaseAddress is a pointer to
     * @return
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) {
        try {
            participatingRestaurants = new ObjectMapper().readValue(serverBaseAddress, Restaurant[].class);
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return participatingRestaurants;
    }

    public Menu[] getMenu() {
        return menu;
    }
}
