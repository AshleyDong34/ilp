package uk.ac.ed.inf;

/**
 * This class takes orders for the participating restaurants
 */
public class Order {
    /**
     * This method calculates the total delivery cost or will throw an exception if the order is
     * not correct with help from the checkPizzaCombination() method
     * @param restaurants is an array of restaurant objects, which is the participating restaurants involved
     * @param pizzas is an array of Strings of pizza orders
     * @return the total cost of the order, which include the delivery charge of 100 pence, if it is a valid order
     */
    public static int getDeliveryCost(Restaurant[] restaurants, String[] pizzas) {
        int totalCost = 100;
        Restaurant pickedRestaurant = null;

        // throws exception if the order size is invalid, where it is larger than 4 or less than 1
        if ((pizzas.length > 4) || (pizzas.length < 1)) {
            try {
                throw new InvalidPizzaCombinationException("Invalid number of pizzas ordered");
            } catch (InvalidPizzaCombinationException e) {
                throw new RuntimeException(e);
            }
        }

        // finds the first restaurant that contains a pizza from the order, otherwise the pickedRestaurant will remain null
        for (int j = 0; j < restaurants.length; j++) {
            for (int k = 0; k < restaurants[j].getMenu().length; k++) {
                if (restaurants[j].getMenu()[k].name.equals(pizzas[0])) {
                    pickedRestaurant = restaurants[j];
                }
            }
        }

        // Checks through the pizza combination and the selected restaurant
        checkPizzaCombination(pickedRestaurant, pizzas);

        //finds the pizzas ordered on the menu of the restaurant and adds onto the total cost
        for (int i = 0; i < pizzas.length; i++) {
            for (int j = 0; j < pickedRestaurant.menu.length; j++) {
                if (pickedRestaurant.menu[j].name.equals(pizzas[i])) {
                    totalCost += pickedRestaurant.menu[j].priceInPence;
                }
            }
        }

        return totalCost;
    }

    /**
     * This method checks the validity of the order combination and throws an exception if the order is invalid
     * @param restaurant is an array of the participating restaurants or null if none of the restaurants matches
     * @param pizzaOrders an array of the String of pizzas in the order
     */
    private static void checkPizzaCombination(Restaurant restaurant, String[] pizzaOrders) {

        // if the restaurant argument is null when passed to this method, it means that no restaurant fits the order description
        // throw an exception
        if (restaurant == null) {
            try {
                throw new InvalidPizzaCombinationException("The ordered pizzas do not come from a participating restaurant");
            } catch (InvalidPizzaCombinationException e) {
                throw new RuntimeException(e);
            }
        }

        // loops through each order item and compares to the restaurant menu items
        for (int i = 0; i < pizzaOrders.length; i++) {
            boolean isSameRestaurant = false;
            for (int j = 0; j < restaurant.menu.length; j++) {
                // if the item does exist on the menu, then the variable is set to true
                if (restaurant.menu[j].name.equals(pizzaOrders[i])) {
                    isSameRestaurant = true;
                }
            }
            // if the variable is set to false, it means an item did not match an of the menu items, throw an exception
            if (!isSameRestaurant) {
                try {
                    throw new InvalidPizzaCombinationException("The input pizza combination is not from the same restaurant");
                } catch (InvalidPizzaCombinationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
