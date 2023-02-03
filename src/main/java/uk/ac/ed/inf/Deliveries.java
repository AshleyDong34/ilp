package uk.ac.ed.inf;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * This class contains the orderNo, order outcome, and costInPence for each instance of this class.
 * All order validation happens in this class. This class has one getter class that creates the necessary array
 * of objects to be returned.
 */
public class Deliveries {

    public String orderNo;
    public OrderOutcome outcome;
    public int costInPence;

    public Deliveries(String orderNO, OrderOutcome outcome, int costInPence) {
        this.outcome = outcome;
        this.orderNo = orderNO;
        this.costInPence = costInPence;
    }

    /**
     * Whenever the getDeliveries method get called, we will create the array of Deliveries and check through the orders.
     * We create a new Deliveries object with the checked order outcome and also its costInPence and add it to the array of Deliveries.
     * This method uses all the other private methods in the Class to choose an OrderOutcome.
     * @param allOrders an array of OrderDetails which needs to be checked
     * @return an array of Deliveries with updated orderOutcomes for each object
     */
    public static Deliveries[] getDeliveries(OrderDetails[] allOrders) {

        Deliveries[] deliveries = new Deliveries[allOrders.length];
        //get the list of restaurants from the rest server
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(BaseAddressAndDate.getBaseAddress());
        // do order validation on all the orders
        for (int i = 0; i < allOrders.length; i++) {
            OrderDetails currentOrder = allOrders[i];
            OrderOutcome outcome1;
            int cost = 0;

            // check through all the possible Invalid order outcomes and select one order outcome
            if (!isCardNumberCorrect(currentOrder)) {
                outcome1 = OrderOutcome.InvalidCardNumber;
            } else if (!(currentOrder.cvv.length() == 3)) {
                outcome1 = OrderOutcome.InvalidCvv;
            } else if ((currentOrder.orderItems.length > 4)) {
                outcome1 = OrderOutcome.InvalidPizzaCount;
            } else if (!checkPizzaIsValid(currentOrder.orderItems, restaurants)) {
                outcome1 = OrderOutcome.InvalidPizzaNotDefined;
            } else if (!checkPizzaCombination(restaurants, currentOrder.orderItems)) {
                outcome1 = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
            } else if (currentOrder.priceTotalInPence != getDeliveryCost(currentOrder.orderItems, restaurants)) {
                outcome1 = OrderOutcome.InvalidTotal;
            } else if (isExpired(currentOrder.orderDate, currentOrder.creditCardExpiry)) {
                outcome1 = OrderOutcome.InvalidExpiryDate;
            } else if (currentOrder.orderNo == null || currentOrder.orderItems == null) {
                outcome1 = OrderOutcome.Invalid;
            } else {
                cost = getDeliveryCost(currentOrder.orderItems, restaurants);
                outcome1 = OrderOutcome.ValidButNotDelivered;
            }
            // add the new object created using the orderNo, outcome and delivery cost.
            deliveries[i] = new Deliveries(currentOrder.orderNo, outcome1, cost);
        }
        return deliveries;
    }

    /**
     * This method uses the Luhn algorithm to check if the card number is correct
     * This is because all cards are based on a pattern. We then check if the cardNumber
     * is the right length of 16 digits.
     * this method not original and is inspired from: <a href="https://www.geeksforgeeks.org/luhn-algorithm/">https://www.geeksforgeeks.org/luhn-algorithm/</a>
     * accessed: online 7th December 2022
     * @param order the order we want to check the card details of
     * @return boolean based on if the card is correct or not, return if the number is correct
     */
    private static Boolean isCardNumberCorrect(OrderDetails order) {
        String cardNo = order.creditCardNumber;
        int nDigits = cardNo.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            // check if the digit is the second number
            isSecond = !isSecond;
        }
        if (nDigits != 16) {
            return false;
        }
        return (nSum % 10 == 0);
    }

    /**
     * This method checks if the order's pizzas have valid names and match up to any
     * of the menus items of all the restaurants
     * @param pizzas the String array of pizzas from the order
     * @param restaurants the array of restaurants from the rest server
     * @return a boolean, true if pizzas are valid, and false otherwise
     */
    private static Boolean checkPizzaIsValid(String[] pizzas, Restaurant[] restaurants) {
        //make a String arraylist that contains all the menu items from all the restaurants
        ArrayList<String> allMenus = new ArrayList<>();
        for (Restaurant restaurant1: restaurants) {
            for (int i = 0; i<restaurant1.getMenu().length; i++) {
                allMenus.add(restaurant1.getMenu()[i].name);
            }
        }
        // turn the pizzas into a List
        List<String> pizzasAsList = Arrays.asList(pizzas);
        // return if the menu contains all the pizza names
        return allMenus.containsAll(pizzasAsList);
    }

    /**
     * This method checks that the pizzas from a single order does not come from different restaurant's menus.
     * @param restaurants the list of restaurants from the rest server
     * @param pizzas the list of pizzas from the order
     * @return a boolean containsPizza, where it is true if the order's pizzas are from the same restaurant
     */
    private static Boolean checkPizzaCombination(Restaurant[] restaurants, String[] pizzas) {

        boolean containsPizza = true;

        // checks all restaurants
        for (Restaurant restaurant : restaurants) {

            // checks through each restaurant's menus
            for (int k = 0; k < restaurant.getMenu().length; k++) {

                // if the menu contains the first pizza from the order
                if (restaurant.getMenu()[k].name.equals(pizzas[0])) {
                    //create an arraylist of type String of the menu from regular String array
                    String[] menu = new String[restaurant.getMenu().length];
                    for (int j = 0; j < restaurant.getMenu().length; j++) {
                        menu[j] = restaurant.getMenu()[j].name;
                    }
                    List<String> listMenu = Arrays.asList(menu);
                    // checks if the pizzas are all from this menu
                    for (String pizza: pizzas) {
                        if (!listMenu.contains(pizza)) {
                            // if one of the pizzas do not match, return false
                            containsPizza = false;
                            break;
                        }
                    }
                }
            }
        }
        return containsPizza;
    }

    /**
     * This method returns the cost of the order items by matching up with the selected restaurant's menu and
     * adding the price to a variable which starts with the delivery cost of 100 pence
     * To find the selected restaurant, we have to find the restaurant that the first order pizza name matches up with.
     * @param pizzas the array of String that is the orderItems for that order
     * @param restaurants the array of participating restaurants
     * @return an integer that totals the full cost
     */
    private static int getDeliveryCost(String[] pizzas, Restaurant[] restaurants) {
        int cost = 100;
        // finds the selected restaurant that has the menu which matches the first pizza
        Restaurant pickedRestaurant = restaurants[0];
        for (Restaurant restaurant : restaurants) {

            for (int k = 0; k < restaurant.getMenu().length; k++) {

                if (restaurant.getMenu()[k].name.equals(pizzas[0])) {
                    pickedRestaurant = restaurant;
                }
            }
        }
        // go through the selected restaurant and find all the items that match the order
        for (int i = 0; i < pickedRestaurant.getMenu().length; i++) {
            for (String pizza: pizzas) {
                if(pickedRestaurant.getMenu()[i].name.equals(pizza)) {
                    // if a match is found, add the item price to the total cost
                    cost += pickedRestaurant.getMenu()[i].priceInPence;
                }
            }
        }
        return cost;
    }

    /**
     * This method checks if the card is expired when compared with the order date
     * The card only expires on the end of the month, we only check the months if the year both match up.
     * @param orderDate String of the orderDate in the format YYYY-MM-DD
     * @param cardExpiryDate String of the Expiry date in the format MM/YY
     * @return boolean result based on if the card is expired or not
     */
    private static boolean isExpired(String orderDate, String cardExpiryDate) {

        // get the year and month in int format from orderDate
        String[] splitOrderDate = orderDate.split("-");
        int year = Integer.parseInt(splitOrderDate[0].trim());
        int month = Integer.parseInt(splitOrderDate[1].trim());

        // get the card year and month in int format from cardExpiryDate
        String[] splitExpiryDate = cardExpiryDate.split("/");
        int cardMonth = Integer.parseInt(splitExpiryDate[0].trim());
        int cardYear = Integer.parseInt(splitExpiryDate[1].trim());

        // we only want the last two digits, so we modulo by 100
        year = year % 100;

        boolean isExpired = true;
        // if the cardYear is larger than the order date, its expired
        if (year < cardYear) {
            isExpired = false;
        }
        // check the month if the year is the same
        else if (year == cardYear) {
            // card month has to be the same or larger than the month
            if (cardMonth >= month) {
                isExpired = false;
            }
        }
        return isExpired;
    }
}
