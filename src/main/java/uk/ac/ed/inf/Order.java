package uk.ac.ed.inf;

public class Order {
    public static int getDeliveryCost(Restaurant[] restaurants, String[] pizzas) {
        int totalCost = 100;
        Restaurant pickedRestaurant = null;


        /**There's several things to check for in this function
         * make sure that all the pizzas come from the same restaurant
         * make sure that it is not an empty order
         *
         */
        if (pizzas.length == 0) {
            try {
                throw new InvalidPizzaCombinationException("Invalid number of pizzas ordered");
            } catch (InvalidPizzaCombinationException e) {
                throw new RuntimeException(e);
            }
        }
        // this needs fixed, as a null order may pass through here
        for (int j = 0; j < restaurants.length; j++) {
            for (int k = 0; k < restaurants[j].getMenu().length; k++) {
                if (restaurants[j].getMenu()[k].name.equals(pizzas[0])) {
                    pickedRestaurant = restaurants[j];
                }
            }
        }
        // null checker starts here
        checkPizzaCombination(pickedRestaurant, pizzas);

        for (int i = 0; i < pizzas.length; i++) {
            for (int j = 0; j < pickedRestaurant.menu.length; j++) {
                if (pickedRestaurant.menu[j].name.equals(pizzas[i])) {
                    totalCost += pickedRestaurant.menu[j].priceInPence;
                }
            }
        }

        return totalCost;
    }


    private static void checkPizzaCombination(Restaurant restaurant, String[] pizzaOrders) {

        if ((pizzaOrders.length > 4) || (pizzaOrders.length < 1)) {
            try {
                throw new InvalidPizzaCombinationException("Invalid number of pizzas ordered");
            } catch (InvalidPizzaCombinationException e) {
                throw new RuntimeException(e);
            }
        }

        if (restaurant == null) {
            try {
                throw new InvalidPizzaCombinationException("The ordered pizzas do not come from a participating restaurant");
            } catch (InvalidPizzaCombinationException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < pizzaOrders.length; i++) {
            boolean isSameRestaurant = false;
            for (int j = 0; j < restaurant.menu.length; j++) {
                if (restaurant.menu[j].name.equals(pizzaOrders[i])) {
                    isSameRestaurant = true;
                }
            }
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
