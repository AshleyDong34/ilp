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
     * This method use the arg provided and turns the json string into an array of the Restaurant class,
     * this is now assigned to the static variable participatingRestaurants
     * @param completeAddress is a String to a resource to the web
     * @return an array of participating restaurants
     */
    public static Restaurant[] getRestaurantsFromRestServer(String completeAddress) {
        try {
            // assign the deserialized data to the variable participatingRestaurants
            completeAddress = completeAddress + "restaurants/";
            URL restaurantsURL = new URL(completeAddress);
            participatingRestaurants = new ObjectMapper().readValue(restaurantsURL, Restaurant[].class);
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return participatingRestaurants;
    }

    /**
     * This method returns the picked restaurant based on the first pizza chosen from the order.
     * The array of restaurants is found by using the getRestaurantsFromRestServer() function
     * @param pizzas the String array of pizzas from this order
     * @return The chosen restaurant based on the first pizza
     */
    public static Restaurant findRestaurant(String[] pizzas) {
        String pizza = pizzas[0];
        Restaurant pickedRestaurant = null;
        // return the array of restaurants from the rest server
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(BaseAddressAndDate.getBaseAddress());
        for (Restaurant restaurant : restaurants) {

            for (int k = 0; k < restaurant.getMenu().length; k++) {

                if (restaurant.getMenu()[k].name.equals(pizza)) {
                    pickedRestaurant = restaurant;
                }
            }
        }
        return pickedRestaurant;
    }

    /**
     * A getter for the menu of this restaurant
     * @return and array of menu objects for this restaurant
     */
    public Menu[] getMenu() {
        return menu;
    }
}
